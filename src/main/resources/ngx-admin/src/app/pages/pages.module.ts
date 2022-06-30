import { NgModule } from '@angular/core';
import { NbMenuModule } from '@nebular/theme';

import { ThemeModule } from '../@theme/theme.module';
import { PagesComponent } from './pages.component';
import { DashboardModule } from './dashboard/dashboard.module';
import { ECommerceModule } from './e-commerce/e-commerce.module';
import { PagesRoutingModule } from './pages-routing.module';
import { MiscellaneousModule } from './miscellaneous/miscellaneous.module';
import { AccountsModule } from './account/account.module';
import { CategoryModule } from './categories/categrory.module';
import { PostModule } from './post/post.module';

@NgModule({
  imports: [
    PagesRoutingModule,
    ThemeModule,
    NbMenuModule,
    DashboardModule,
    ECommerceModule,
    AccountsModule,
    MiscellaneousModule,
    CategoryModule,
    PostModule,
  ],
  declarations: [
    PagesComponent,
  ],
})
export class PagesModule {
}
