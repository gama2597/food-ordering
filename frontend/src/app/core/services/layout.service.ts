import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LayoutService {
  readonly mobile = signal(false);
  readonly sidebarOpen = signal(true);

  setMobile(isMobile: boolean): void {
    this.mobile.set(isMobile);
    this.sidebarOpen.set(!isMobile);
  }

  toggleSidebar(): void {
    this.sidebarOpen.set(!this.sidebarOpen());
  }

  closeSidebar(): void {
    if (this.mobile()) {
      this.sidebarOpen.set(false);
    }
  }
}
