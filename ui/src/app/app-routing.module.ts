import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './views/landing/landing.component';
import { AboutComponent } from './views/main/about/about.component';
import { CourtDetailComponent } from './views/main/court-detail/court-detail.component';
import { DashboardComponent } from './views/main/dashboard/dashboard.component';
import { MainComponent } from './views/main/main.component';
import {TournamentDetailComponent} from "./views/main/tournament-detail/tournament-detail.component";

const routes: Routes = [
  { path: 'landing', component: LandingComponent },
  {
    path: 'main',
    component: MainComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'about', component: AboutComponent },
      { path: 'court/:id', component: CourtDetailComponent },
      { path: 'tournament/:id', component: TournamentDetailComponent},
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
