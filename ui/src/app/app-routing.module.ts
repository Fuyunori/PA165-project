import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { LandingComponent } from './views/landing/landing.component';
import { AboutComponent } from './views/main/about/about.component';
import { CourtDetailComponent } from './views/main/court-detail/court-detail.component';
import { DashboardComponent } from './views/main/dashboard/dashboard.component';
import { MainComponent } from './views/main/main.component';

const routes: Routes = [
  {
    path: 'landing',
    component: LandingComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'main',
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'about', component: AboutComponent },
      { path: 'court/:id', component: CourtDetailComponent },
      { path: '**', redirectTo: 'dashboard' },
    ],
  },
  { path: '**', redirectTo: 'landing' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
