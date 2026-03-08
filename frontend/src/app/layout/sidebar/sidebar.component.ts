import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CardModule } from 'primeng/card';
import { LayoutService } from '../../core/services/layout.service';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CardModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  constructor(
    private readonly layoutService: LayoutService,
    private readonly authService: AuthService
  ) {}

  readonly navItems = [
    { label: 'Dashboard', icon: 'pi pi-chart-line', link: '/dashboard', roles: ['CUSTOMER', 'ADMIN', 'RESTAURANT', 'COURIER'] },
    { label: 'Usuarios', icon: 'pi pi-user', link: '/usuarios', roles: ['CUSTOMER', 'ADMIN', 'RESTAURANT', 'COURIER'] },
    { label: 'Productos', icon: 'pi pi-list', link: '/productos', roles: ['CUSTOMER', 'ADMIN', 'RESTAURANT'] }
  ];

  canViewItem(itemRoles: string[]): boolean {
    return this.authService.hasAnyRole(itemRoles);
  }

  closeOnMobile(): void {
    this.layoutService.closeSidebar();
  }
}
