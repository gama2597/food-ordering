import { Injectable, signal } from '@angular/core';
import Keycloak from 'keycloak-js';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly keycloak = new Keycloak({
    url: environment.keycloak.url,
    realm: environment.keycloak.realm,
    clientId: environment.keycloak.clientId
  });

  readonly authenticated = signal(false);
  readonly username = signal<string>('');
  readonly roles = signal<string[]>([]);

  private readonly inactivityLimitMs = 15 * 60 * 1000;
  private readonly refreshThrottleMs = 15000;
  private readonly minTokenValiditySeconds = 60;

  private lastActivityAt = Date.now();
  private lastActivityMarkAt = 0;
  private lastRefreshAttemptAt = 0;
  private sessionMonitorIntervalId: number | null = null;
  private refreshPromise: Promise<boolean> | null = null;
  private expiringSession = false;

  private readonly activityHandler = () => {
    const now = Date.now();
    if (now - this.lastActivityMarkAt < 5000) {
      return;
    }
    this.lastActivityAt = now;
    this.lastActivityMarkAt = now;
  };

  async initialize(): Promise<void> {
    const authenticated = await this.keycloak.init({
      onLoad: 'check-sso',
      checkLoginIframe: false,
      pkceMethod: 'S256'
    });

    this.authenticated.set(authenticated);
    this.username.set((this.keycloak.tokenParsed?.['preferred_username'] as string) ?? 'Usuario');
    this.roles.set(this.extractRoles());

    if (authenticated) {
      this.startSessionMonitoring();
    }
  }

  async login(): Promise<void> {
    await this.keycloak.login();
  }

  async logout(): Promise<void> {
    this.stopSessionMonitoring();
    await this.keycloak.logout({ redirectUri: window.location.origin });
  }

  isAuthenticated(): boolean {
    return this.authenticated();
  }

  hasAnyRole(expectedRoles: string[]): boolean {
    if (expectedRoles.length === 0) {
      return true;
    }

    const currentRoles = this.roles().map((role) => role.toUpperCase());
    if (currentRoles.length === 0) {
      return this.authenticated();
    }

    return expectedRoles.some((expectedRole) => {
      const normalized = expectedRole.toUpperCase();
      return (
        currentRoles.includes(normalized) ||
        currentRoles.includes(`ROLE_${normalized}`) ||
        currentRoles.includes(normalized.replace('ROLE_', ''))
      );
    });
  }

  getToken(): string | null {
    return this.keycloak.token ?? null;
  }

  async ensureValidToken(force = false): Promise<boolean> {
    if (!this.authenticated()) {
      return false;
    }

    const now = Date.now();
    if (!force && now - this.lastRefreshAttemptAt < this.refreshThrottleMs) {
      return true;
    }

    if (this.refreshPromise) {
      return this.refreshPromise;
    }

    this.lastRefreshAttemptAt = now;
    this.refreshPromise = this.keycloak
      .updateToken(this.minTokenValiditySeconds)
      .then(() => {
        this.roles.set(this.extractRoles());
        return true;
      })
      .catch(async () => {
        await this.expireSession();
        return false;
      })
      .finally(() => {
        this.refreshPromise = null;
      });

    return this.refreshPromise;
  }

  private startSessionMonitoring(): void {
    this.stopSessionMonitoring();

    this.lastActivityAt = Date.now();
    this.lastActivityMarkAt = 0;
    this.bindActivityListeners();

    this.sessionMonitorIntervalId = window.setInterval(() => {
      this.handleSessionTick();
    }, 30000);
  }

  private stopSessionMonitoring(): void {
    this.unbindActivityListeners();
    if (this.sessionMonitorIntervalId !== null) {
      clearInterval(this.sessionMonitorIntervalId);
      this.sessionMonitorIntervalId = null;
    }
  }

  private bindActivityListeners(): void {
    window.addEventListener('click', this.activityHandler, { passive: true });
    window.addEventListener('keydown', this.activityHandler, { passive: true });
    window.addEventListener('scroll', this.activityHandler, { passive: true });
    window.addEventListener('mousemove', this.activityHandler, { passive: true });
    window.addEventListener('touchstart', this.activityHandler, { passive: true });
  }

  private unbindActivityListeners(): void {
    window.removeEventListener('click', this.activityHandler);
    window.removeEventListener('keydown', this.activityHandler);
    window.removeEventListener('scroll', this.activityHandler);
    window.removeEventListener('mousemove', this.activityHandler);
    window.removeEventListener('touchstart', this.activityHandler);
  }

  private handleSessionTick(): void {
    if (!this.authenticated()) {
      return;
    }

    const now = Date.now();
    const inactivityMs = now - this.lastActivityAt;
    if (inactivityMs >= this.inactivityLimitMs) {
      void this.expireSession();
      return;
    }

    if (inactivityMs <= 2 * 60 * 1000) {
      void this.ensureValidToken();
    }
  }

  private async expireSession(): Promise<void> {
    if (this.expiringSession) {
      return;
    }
    this.expiringSession = true;
    this.stopSessionMonitoring();
    this.authenticated.set(false);
    this.username.set('');

    await this.keycloak.logout({
      redirectUri: `${window.location.origin}/session-expired`
    });
  }

  private extractRoles(): string[] {
    const claims = this.keycloak.tokenParsed as Record<string, unknown> | undefined;
    const realmAccess = claims?.['realm_access'] as { roles?: unknown } | undefined;
    const realmRoles = Array.isArray(realmAccess?.roles)
      ? realmAccess.roles.filter((role): role is string => typeof role === 'string')
      : [];

    const resourceAccess = claims?.['resource_access'] as Record<string, { roles?: unknown }> | undefined;
    const clientRolesRaw = resourceAccess?.[environment.keycloak.clientId]?.roles;
    const clientRoles = Array.isArray(clientRolesRaw)
      ? clientRolesRaw.filter((role): role is string => typeof role === 'string')
      : [];

    return [...new Set([...realmRoles, ...clientRoles])];
  }
}
