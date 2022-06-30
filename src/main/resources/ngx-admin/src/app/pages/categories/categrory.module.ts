import { NgModule } from '@angular/core';
import {
  NbActionsModule,
  NbButtonModule,
  NbCardModule,
  NbTabsetModule,
  NbUserModule,
  NbRadioModule,
  NbSelectModule,
  NbListModule,
  NbIconModule,
  NbInputModule,
  NbCheckboxModule,
  NbDatepickerModule,
} from '@nebular/theme';
import { NgxEchartsModule } from 'ngx-echarts';

import { ThemeModule } from '../../@theme/theme.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CategoryComponent } from './category.component';
import { CategoryDialogComponent } from './dialog/dialog.component';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { CommonModule } from '@angular/common';
import { AccountsRoutingModule } from '../account/account-routing.module';

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ThemeModule,
    CommonModule,
    NbInputModule,
    NbCardModule,
    NbButtonModule,
    NbActionsModule,
    NbUserModule,
    NbCheckboxModule,
    NbRadioModule,
    NbDatepickerModule,
    AccountsRoutingModule,
    NbSelectModule,
    NbIconModule,
    Ng2SmartTableModule,
  ],
  declarations: [
    CategoryComponent,
    CategoryDialogComponent,
  ],
})
export class CategoryModule { }
