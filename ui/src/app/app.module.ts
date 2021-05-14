import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './views/landing/landing.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { MainComponent } from './views/main/main.component';
import { DashboardComponent } from './views/main/dashboard/dashboard.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CourtCardComponent } from './components/court-card/court-card.component';
import { CourtListComponent } from './components/court-list/court-list.component';
import { CourtFormComponent } from './components/court-form/court-form.component';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { CourtDetailComponent } from './views/main/court-detail/court-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    MainComponent,
    DashboardComponent,
    CourtCardComponent,
    CourtListComponent,
    CourtFormComponent,
    CourtDetailComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatToolbarModule,
    MatDialogModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
