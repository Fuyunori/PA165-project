import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { LandingComponent } from './views/landing/landing.component';
import { AboutComponent } from './views/main/about/about.component';
import { CourtDetailComponent } from './views/main/court-detail/court-detail.component';
import { DashboardComponent } from './views/main/dashboard/dashboard.component';
import { MainComponent } from './views/main/main.component';
import { LessonDetailComponent } from './views/main/lesson-detail/lesson-detail.component';
import { TournamentDetailComponent } from './views/main/tournament-detail/tournament-detail.component';
import { UserDetailComponent } from './views/main/user-detail/user-detail.component';
import { UsersOverviewComponent } from './views/main/users-overview/users-overview.component';
import {BookingDetailComponent} from "./views/main/booking-detail/booking-detail.component";
import { ManagerGuard } from './guards/manager.guard';
import { UserIdGuard } from './guards/user-id.guard';

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
      { path: 'lesson/:id', component: LessonDetailComponent },
      { path: 'tournament/:id', component: TournamentDetailComponent },
      { path: 'booking/:id', component: BookingDetailComponent },
      {
        path: 'user/:id',
        component: UserDetailComponent,
        canActivate: [UserIdGuard],
      },
      {
        path: 'users',
        component: UsersOverviewComponent,
        canActivate: [ManagerGuard],
      },
      { path: '**', redirectTo: 'dashboard' },
    ],
  },
  { path: '**', redirectTo: 'landing' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
