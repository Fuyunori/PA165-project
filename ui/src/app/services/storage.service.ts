import { Injectable } from '@angular/core';

const TOKEN_KEY = 'tennisClubToken';

@Injectable({ providedIn: 'root' })
export class StorageService {
  private readonly storage: Storage = sessionStorage;

  get token(): string | null {
    return this.storage.getItem(TOKEN_KEY);
  }

  set token(token: string | null) {
    if (token != null) {
      this.storage.setItem(TOKEN_KEY, token);
    } else {
      this.storage.removeItem(TOKEN_KEY);
    }
  }
}
