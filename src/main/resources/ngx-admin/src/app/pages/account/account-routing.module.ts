import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RoleComponent } from './roles/role.component';
import { AccountComponent } from './account.component';
import { UserComponent } from './users/user.component';
const routes: Routes = [
  {
    path: '',
    component: AccountComponent,
    children: [
      {
        path: 'roles',
        component: RoleComponent,
      },
      {
        path: 'users',
        component: UserComponent,
      },
    ],
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
  ],
  exports: [
    RouterModule,
  ],
})
export class AccountsRoutingModule {
}

