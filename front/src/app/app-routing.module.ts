import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DefaultLayoutComponent, DefaultLayoutOpenComponent } from './containers';
import { Page404Component } from './views/pages/page404/page404.component';
import { Page500Component } from './views/pages/page500/page500.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './views/pages/register/register.component';
import { OrganizationComponent } from './organization/organization.component';
import { UserComponent } from './user/user.component';
import { CategoryComponent } from './category/category.component';
import { IndicatorComponent } from './indicator/indicator.component';
import { IndicatorValueComponent } from './indicators_value/indicatorvalue.component';
import { DashboardSetupComponent } from './dashboard_setup/dashboardsetup.component';
import { AuthGuard, LoginGuard } from './auth.guard';
import { DomainComponent } from './domain/domain.component';
import { CompositeIndicatorComponent } from './compositeindicator/compositeindicator.component';
import { CompositeIndicatorValueComponent } from './composite-indicator-value/compositeindicatorvalue.component';
import { DboardComponent } from './dboard/dboard.component';
import { PopulationComponent } from './population/population.component';
import { BasicInformationComponent } from './basic-information/basic-information.component';
import { InfographicsComponent } from './infographics/infographics.component';
import { ResourceComponent } from './resources/resource.component';
import {GalleryComponent} from './gallery/gallery.component';
import {TrialComponent} from './trial/trial.component';
import { DashboardComponent } from './views/dashboard/dashboard.component';
import { FaqComponent } from './faq/faq.component';


const routes: Routes = [
  {
    path: '',
    // component: DefaultLayoutComponent,
    redirectTo: 'dashboard',
    // canActivate: [LoginGuard],
    pathMatch: 'full'
  },
  // {
  //   path: '',
  //   component: LoginComponent,
  //   canActivate: [LoginGuard],
  //   data: {
  //     title: 'Login Page'
  //   }
  // },

  
  {
    path: '',
    component: DefaultLayoutOpenComponent,
    // canActivate: [LoginGuard],
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./views/dashboard/dashboard.module').then((m) => m.DashboardModule)
      },
      {
        path: 'login',
        component: LoginComponent,
        data: {
          title: 'Login Page'
        }
      },
      {
        path: 'portal',
        component: TrialComponent,
        data: {
          title: 'Portal Page'
        }
      },
      // {
      //   path: 'organization',
      //   component: OrganizationComponent
      // },
      // {
      //   path: 'basic-information',
      //   component: BasicInformationComponent
      // },
      // {
      //   path: 'infographics',
      //   component: InfographicsComponent
      // },
      // {
      //   path: 'infographics/gallery',
      //   component: GalleryComponent
      // },
      // {
      //   path: 'trial',
      //   component: TrialComponent
      // },
      // {
      //   path: 'user',
      //   component: UserComponent
      // },
      // {
      //   path: 'resources',
      //   component: ResourceComponent
      // },
      // {
      //   path: 'category',
      //   component: CategoryComponent
      // },
      // {
      //   path: 'indicators',
      //   component: IndicatorComponent
      // },
      // {
      //   path: 'indicators-value',
      //   component: IndicatorValueComponent
      // },
      // {
      //   path: 'dashboard-setup',
      //   component: DashboardSetupComponent
      // },
      // {
      //   path: 'dboard',
      //   component: DboardComponent
      // },
      // {
      //   path: 'domain',
      //   component: DomainComponent
      // },
      // {
      //   path: 'composite-indicator',
      //   component: CompositeIndicatorComponent
      // },
      // {
      //   path: 'composite-indicator-value',
      //   component: CompositeIndicatorValueComponent
      // },
      {
        path: 'theme',
        loadChildren: () =>
          import('./views/theme/theme.module').then((m) => m.ThemeModule)
      },
      {
        path: 'base',
        loadChildren: () =>
          import('./views/base/base.module').then((m) => m.BaseModule)
      },
      {
        path: 'buttons',
        loadChildren: () =>
          import('./views/buttons/buttons.module').then((m) => m.ButtonsModule)
      },
      {
        path: 'forms',
        loadChildren: () =>
          import('./views/forms/forms.module').then((m) => m.CoreUIFormsModule)
      },
      {
        path: 'charts',
        loadChildren: () =>
          import('./views/charts/charts.module').then((m) => m.ChartsModule)
      },
      {
        path: 'icons',
        loadChildren: () =>
          import('./views/icons/icons.module').then((m) => m.IconsModule)
      },
      {
        path: 'notifications',
        loadChildren: () =>
          import('./views/notifications/notifications.module').then((m) => m.NotificationsModule)
      },
      {
        path: 'widgets',
        loadChildren: () =>
          import('./views/widgets/widgets.module').then((m) => m.WidgetsModule)
      },
      {
        path: 'pages',
        loadChildren: () =>
          import('./views/pages/pages.module').then((m) => m.PagesModule)
      },
    ]
  },



  {
    path: '',
    component: DefaultLayoutComponent,
    canActivate: [AuthGuard],
    data: {
      title: 'Home'
    },
    children: [
      // {
      //   path: 'dashboard',
      //   loadChildren: () =>
      //     import('./views/dashboard/dashboard.module').then((m) => m.DashboardModule)
      // },
      {
        path: 'organization',
        component: OrganizationComponent
      },
      {
        path: 'basic-information',
        component: BasicInformationComponent
      },
      {
        path: 'infographics',
        component: InfographicsComponent
      },
      {
        path: 'infographics/gallery',
        component: GalleryComponent
      },
      // {
      //   path: 'trial',
      //   component: TrialComponent
      // },
      {
        path: 'user',
        component: UserComponent
      },
      {
        path: 'faq',
        component: FaqComponent
      },
      {
        path: 'resources',
        component: ResourceComponent
      },
      {
        path: 'category',
        component: CategoryComponent
      },
      {
        path: 'indicators',
        component: IndicatorComponent
      },
      {
        path: 'indicators-value',
        component: IndicatorValueComponent
      },
      {
        path: 'population',
        component: PopulationComponent
      },
      {
        path: 'dashboard-setup',
        component: DashboardSetupComponent
      },
      {
        path: 'dboard',
        component: DboardComponent
      },
      {
        path: 'domain',
        component: DomainComponent
      },
      {
        path: 'composite-indicator',
        component: CompositeIndicatorComponent
      },
      {
        path: 'composite-indicator-value',
        component: CompositeIndicatorValueComponent
      },
      // {
      //   path: 'theme',
      //   loadChildren: () =>
      //     import('./views/theme/theme.module').then((m) => m.ThemeModule)
      // },
      // {
      //   path: 'base',
      //   loadChildren: () =>
      //     import('./views/base/base.module').then((m) => m.BaseModule)
      // },
      // {
      //   path: 'buttons',
      //   loadChildren: () =>
      //     import('./views/buttons/buttons.module').then((m) => m.ButtonsModule)
      // },
      // {
      //   path: 'forms',
      //   loadChildren: () =>
      //     import('./views/forms/forms.module').then((m) => m.CoreUIFormsModule)
      // },
      // {
      //   path: 'charts',
      //   loadChildren: () =>
      //     import('./views/charts/charts.module').then((m) => m.ChartsModule)
      // },
      // {
      //   path: 'icons',
      //   loadChildren: () =>
      //     import('./views/icons/icons.module').then((m) => m.IconsModule)
      // },
      // {
      //   path: 'notifications',
      //   loadChildren: () =>
      //     import('./views/notifications/notifications.module').then((m) => m.NotificationsModule)
      // },
      // {
      //   path: 'widgets',
      //   loadChildren: () =>
      //     import('./views/widgets/widgets.module').then((m) => m.WidgetsModule)
      // },
      // {
      //   path: 'pages',
      //   loadChildren: () =>
      //     import('./views/pages/pages.module').then((m) => m.PagesModule)
      // },
    ]
  },





  {
    path: '404',
    component: Page404Component,
    data: {
      title: 'Page 404'
    }
  },
  {
    path: '500',
    component: Page500Component,
    data: {
      title: 'Page 500'
    }
  },
  
  {
    path: 'register',
    component: RegisterComponent,
    data: {
      title: 'Register Page'
    }
  },
  {path: '**', redirectTo: 'dboard'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'top',
      anchorScrolling: 'enabled',
      initialNavigation: 'enabledBlocking'
      // relativeLinkResolution: 'legacy'
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
