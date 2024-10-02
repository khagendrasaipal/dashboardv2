import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule,FormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';

import { NgSelectModule } from '@ng-select/ng-select';
import { CarouselModule } from 'ngx-bootstrap/carousel';

import {
  AvatarModule,
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  // CarouselModule,
  FormModule,
  GridModule,
  NavModule,
  ProgressModule,
  TableModule,
  TabsModule
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';

import { WidgetsModule } from '../widgets/widgets.module';

import { ChartjsModule } from '@coreui/angular-chartjs';
import { ChartModule } from 'angular-highcharts';

@NgModule({
  imports: [
    DashboardRoutingModule,
    CardModule,
    NavModule,
    IconModule,
    TabsModule,
    CommonModule,
    GridModule,
    ProgressModule,
    ReactiveFormsModule,
    ButtonModule,
    FormModule,
    ButtonModule,
    ButtonGroupModule,
    ChartjsModule,
    ChartModule,
    AvatarModule,
    TableModule,
    WidgetsModule,
    PaginationModule.forRoot(),    
    FormsModule,
    NgSelectModule,
    CarouselModule
  ],
  declarations: [DashboardComponent]
})
export class DashboardModule {
}
