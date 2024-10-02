import { NgModule } from '@angular/core';
import { HashLocationStrategy, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { OrganizationComponent } from './organization/organization.component';
import { LoginComponent } from './login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { AuthInterceptor } from './auth-interceptor';
import { CategoryComponent } from './category/category.component';
import { IndicatorComponent } from './indicator/indicator.component';
import { IndicatorValueComponent } from './indicators_value/indicatorvalue.component';
import { AuthGuard, LoginGuard } from './auth.guard';
import { LoginService } from './login/login.service';
import { ApiService } from './api.service';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { UserComponent } from './user/user.component';
import { DashboardSetupComponent } from './dashboard_setup/dashboardsetup.component';
import { DomainComponent } from './domain/domain.component';
import { CompositeIndicatorComponent } from './compositeindicator/compositeindicator.component';
import { CompositeIndicatorValueComponent } from './composite-indicator-value/compositeindicatorvalue.component';
import { DboardComponent } from './dboard/dboard.component';
import { ResourceComponent } from './resources/resource.component';
import { ChartjsModule } from '@coreui/angular-chartjs';
import { ChartModule } from 'angular-highcharts';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgToggleModule } from 'ngx-toggle-button';
import {BasicInformationComponent} from './basic-information/basic-information.component';
import {InfographicsComponent} from './infographics/infographics.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { ImageCropperModule } from 'ngx-image-cropper';
import { EditorModule } from '@tinymce/tinymce-angular';
import { CKEditorModule } from 'ng2-ckeditor';


import {
  PERFECT_SCROLLBAR_CONFIG,
  PerfectScrollbarConfigInterface,
  PerfectScrollbarModule,
} from 'ngx-perfect-scrollbar';

// Import routing module
import { AppRoutingModule } from './app-routing.module';

// Import app component
import { AppComponent } from './app.component';

// Import containers
import {
  DefaultFooterComponent,
  DefaultHeaderComponent,
  DefaultLayoutComponent,
  DefaultLayoutOpenComponent,
} from './containers';




import {
  AvatarModule,
  BadgeModule,
  BreadcrumbModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  CardTitleDirective,
  DropdownModule,
  FooterModule,
  FormModule,
  GridModule,
  HeaderModule,
  ListGroupModule,
  NavModule,
  ProgressModule,
  SharedModule,
  SidebarModule,
  TabsModule,
  UtilitiesModule,
  
} from '@coreui/angular';

import { IconModule, IconSetService } from '@coreui/icons-angular';
import { GalleryComponent } from './gallery/gallery.component';
import { TrialComponent } from './trial/trial.component';
import { PopulationComponent } from './population/population.component';
import { FaqComponent } from './faq/faq.component';


const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
};

const APP_CONTAINERS = [
  DefaultFooterComponent,
  DefaultHeaderComponent,
  DefaultLayoutComponent,
  DefaultLayoutOpenComponent,
 

];


@NgModule({
  declarations: [AppComponent,FaqComponent,ResourceComponent,OrganizationComponent,LoginComponent,CategoryComponent,IndicatorComponent,DashboardSetupComponent,UserComponent,PopulationComponent,
    IndicatorValueComponent,InfographicsComponent,BasicInformationComponent,DomainComponent,CompositeIndicatorComponent,CompositeIndicatorValueComponent,DboardComponent, ...APP_CONTAINERS, GalleryComponent, TrialComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    AvatarModule,
    BreadcrumbModule,
    FooterModule,
    DropdownModule,
    GridModule,
    HeaderModule,
    NgToggleModule,
    SidebarModule,
    IconModule,
    PerfectScrollbarModule,
    NavModule,
    ButtonModule,
    FormModule,
    UtilitiesModule,
    ButtonGroupModule,
    FormsModule,
    ReactiveFormsModule,
    SidebarModule,
    SharedModule,
    TabsModule,
    ChartjsModule,
    ListGroupModule,
    ProgressModule,
    BadgeModule,
    HttpClientModule,
    ListGroupModule,
    CardModule,
    ChartModule,
    ToastrModule.forRoot(),
    PaginationModule.forRoot(),
    ModalModule.forRoot(),
    NgSelectModule,
    ImageCropperModule,
    EditorModule,
    CKEditorModule,
    

  ],
  
  providers: [
    ApiService,
    LoginService,
    AuthGuard,
    LoginGuard,
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy,
    },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG,
    },
    IconSetService,
    Title
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
