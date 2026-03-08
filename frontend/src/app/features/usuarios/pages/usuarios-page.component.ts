import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { InputSwitchModule } from 'primeng/inputswitch';
import { MessageService } from 'primeng/api';
import { UserService } from '../services/user.service';
import { CreateUserAddressRequest, UpdateUserAddressRequest, UserAddress, UserProfile } from '../../../shared/models/user.models';

@Component({
  selector: 'app-usuarios-page',
  standalone: true,
  imports: [CardModule, TableModule, InputTextModule, ButtonModule, InputSwitchModule, FormsModule],
  templateUrl: './usuarios-page.component.html',
  styleUrl: './usuarios-page.component.scss'
})
export class UsuariosPageComponent implements OnInit {
  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);

  profile?: UserProfile;
  addresses: UserAddress[] = [];
  savingAddress = false;
  updatingAddress = false;
  deletingAddressId?: number;
  editingAddressId?: number;

  newAddress: CreateUserAddressRequest = {
    label: '',
    line1: '',
    line2: '',
    district: '',
    city: '',
    reference: '',
    primaryAddress: false
  };

  editAddress: UpdateUserAddressRequest = {
    label: '',
    line1: '',
    line2: '',
    district: '',
    city: '',
    reference: '',
    primaryAddress: false
  };

  ngOnInit(): void {
    this.userService.getMyProfile().subscribe((profile) => {
      this.profile = profile;
    });

    this.loadAddresses();
  }

  addAddress(): void {
    if (!this.newAddress.label || !this.newAddress.line1 || !this.newAddress.district || !this.newAddress.city) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Datos incompletos',
        detail: 'Completa etiqueta, direccion, distrito y ciudad.'
      });
      return;
    }

    this.savingAddress = true;
    this.userService.addAddress(this.newAddress).subscribe({
      next: () => {
        this.savingAddress = false;
        this.newAddress = {
          label: '',
          line1: '',
          line2: '',
          district: '',
          city: '',
          reference: '',
          primaryAddress: false
        };
        this.loadAddresses();
        this.messageService.add({
          severity: 'success',
          summary: 'Direccion agregada',
          detail: 'La direccion se registro correctamente.'
        });
      },
      error: (error) => {
        this.savingAddress = false;
        this.messageService.add({
          severity: 'error',
          summary: 'No se pudo agregar direccion',
          detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
        });
      }
    });
  }

  startEditAddress(address: UserAddress): void {
    this.editingAddressId = address.id;
    this.editAddress = this.cloneAddressPayload(address);
  }

  cancelEditAddress(): void {
    this.editingAddressId = undefined;
  }

  updateAddress(): void {
    if (!this.editingAddressId) {
      return;
    }

    if (!this.isAddressPayloadValid(this.editAddress)) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Datos incompletos',
        detail: 'Completa etiqueta, direccion, distrito y ciudad.'
      });
      return;
    }

    this.updatingAddress = true;
    this.userService.updateAddress(this.editingAddressId, this.editAddress).subscribe({
      next: () => {
        this.updatingAddress = false;
        this.editingAddressId = undefined;
        this.loadAddresses();
        this.messageService.add({
          severity: 'success',
          summary: 'Direccion actualizada',
          detail: 'Se guardaron los cambios correctamente.'
        });
      },
      error: (error) => {
        this.updatingAddress = false;
        this.messageService.add({
          severity: 'error',
          summary: 'No se pudo actualizar direccion',
          detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
        });
      }
    });
  }

  deleteAddress(address: UserAddress): void {
    const confirmed = window.confirm(`Se eliminara la direccion "${address.label}". Esta accion no se puede deshacer.`);
    if (!confirmed) {
      return;
    }

    this.deletingAddressId = address.id;
    this.userService.deleteAddress(address.id).subscribe({
      next: () => {
        this.deletingAddressId = undefined;
        if (this.editingAddressId === address.id) {
          this.editingAddressId = undefined;
        }
        this.loadAddresses();
        this.messageService.add({
          severity: 'success',
          summary: 'Direccion eliminada',
          detail: 'La direccion se desactivo correctamente.'
        });
      },
      error: (error) => {
        this.deletingAddressId = undefined;
        this.messageService.add({
          severity: 'error',
          summary: 'No se pudo eliminar direccion',
          detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
        });
      }
    });
  }

  private loadAddresses(): void {
    this.userService.getMyAddresses().subscribe((addresses) => {
      this.addresses = addresses;
    });
  }

  private isAddressPayloadValid(payload: CreateUserAddressRequest | UpdateUserAddressRequest): boolean {
    return Boolean(payload.label && payload.line1 && payload.district && payload.city);
  }

  private cloneAddressPayload(address: UserAddress): UpdateUserAddressRequest {
    return {
      label: address.label,
      line1: address.line1,
      line2: address.line2,
      district: address.district,
      city: address.city,
      reference: address.reference,
      primaryAddress: address.primaryAddress
    };
  }
}
