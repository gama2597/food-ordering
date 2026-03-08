import { Component, inject } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-session-expired-page',
  standalone: true,
  imports: [CardModule, ButtonModule],
  templateUrl: './session-expired-page.component.html',
  styleUrl: './session-expired-page.component.scss'
})
export class SessionExpiredPageComponent {
  private readonly authService = inject(AuthService);

  relogin(): void {
    void this.authService.login();
  }
}
