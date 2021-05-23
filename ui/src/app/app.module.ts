import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './views/landing/landing.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { MainComponent } from './views/main/main.component';
import { DashboardComponent } from './views/main/dashboard/dashboard.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CourtCardComponent } from './components/court-card/court-card.component';
import { CourtListComponent } from './components/court-list/court-list.component';
import { CourtFormComponent } from './components/court-form/court-form.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { CourtDetailComponent } from './views/main/court-detail/court-detail.component';
import { AboutComponent } from './views/main/about/about.component';
import { ToastrModule } from 'ngx-toastr';
import { LessonFormComponent } from './components/lesson-form/lesson-form.component';
import { LessonDetailComponent } from './views/main/lesson-detail/lesson-detail.component';
import { TournamentFormComponent } from './components/tournament-form/tournament-form.component';
import { TournamentDetailComponent } from './views/main/tournament-detail/tournament-detail.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { LessonStudentsComponent } from './components/lesson-students-list/lesson-students-list.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { UserFormComponent } from './components/user-form/user-form.component';
import { UserDetailComponent } from './views/main/user-detail/user-detail.component';
import { UsersOverviewComponent } from './views/main/users-overview/users-overview.component';
import { UsersTableComponent } from './components/users-table/users-table.component';
import { MatTableModule } from '@angular/material/table';
import { BookingFormComponent } from './components/booking-form/booking-form.component';
import { BookingDetailComponent } from './views/main/booking-detail/booking-detail.component';
import { MatListModule } from '@angular/material/list';
import { AddUserViewComponent } from './components/add-user-view/add-user-view.component';
import { SearchUserPipe } from './pipes/search-user.pipe';
import { LessonTeachersListComponent } from './components/lesson-teachers-list/lesson-teachers-list.component';
import { TournamentRankingComponent } from './components/tournament-ranking/tournament-ranking.component';
import { UserStatisticsComponent } from './components/user-statistics/user-statistics.component';
import { MatTabsModule } from '@angular/material/tabs';
import { EventListComponent } from './components/event-list/event-list.component';
import { SortRankingsPipe } from './pipes/sort-rankings.pipe';
import { SortEventsPipe } from './pipes/sort-events.pipe';
import { RankPlayerDialogComponent } from './components/rank-player-dialog/rank-player-dialog.component';

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
    AboutComponent,
    LessonFormComponent,
    LessonDetailComponent,
    TournamentFormComponent,
    TournamentDetailComponent,
    LessonStudentsComponent,
    UserFormComponent,
    UserDetailComponent,
    UsersOverviewComponent,
    UsersTableComponent,
    BookingFormComponent,
    BookingDetailComponent,
    AddUserViewComponent,
    SearchUserPipe,
    LessonTeachersListComponent,
    TournamentRankingComponent,
    UserStatisticsComponent,
    EventListComponent,
    SortRankingsPipe,
    SortEventsPipe,
    RankPlayerDialogComponent,
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
    MatSelectModule,
    MatButtonModule,
    MatToolbarModule,
    MatDialogModule,
    MatIconModule,
    MatExpansionModule,
    MatTableModule,
    MatListModule,
    MatTabsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-center',
    }),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
