import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Court } from '../models/court.model';

const RESOURCE_URL = `${environment.apiBaseUrl}/courts`;

@Injectable({ providedIn: 'root' })
export class CourtService {
  private readonly state$ = new BehaviorSubject<Court[]>([]);

  readonly courts$ = this.state$.asObservable();

  constructor(private readonly http: HttpClient) {}

  getCourts(): void {
    this.http.get<Court[]>(RESOURCE_URL).subscribe(courts => {
      this.state$.next(courts);
    });
  }

  postCourt(court: Court): void {
    this.http.post<Court>(RESOURCE_URL, court).subscribe(resCourt => {
      const { value } = this.state$;
      this.state$.next([...value, resCourt]);
    });
  }

  putCourt(court: Court): void {
    this.http
      .put<Court>(`${RESOURCE_URL}/${court.id}`, court)
      .subscribe(resCourt => {
        const { value } = this.state$;
        this.state$.next(
          value.map(c => (c.id === resCourt.id ? resCourt : c)),
        );
      });
  }

  deleteCourt(court: Court): void {
    this.http.delete(`${RESOURCE_URL}/${court.id}`).subscribe(() => {
      const { value } = this.state$;
      this.state$.next(value.filter(c => c.id === court.id));
    });
  }
}
