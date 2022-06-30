import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ServerDataSource } from 'ng2-smart-table';
import { UserService } from './user.service';
import { NbDialogService } from '@nebular/theme';
import { UserDialogComponent } from './dialog/dialog.component';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'ngx-user',
  templateUrl: 'user.component.html',
  styleUrls: ['user.component.scss'],
})
export class UserComponent {

  constructor(protected service: UserService,
    private dialogService: NbDialogService,
    private _http: HttpClient) {
  }

  settings = {
    actions: false,
    hideSubHeader: true,
    pager: {
      perPage: environment.pageSize,
    },
    columns: {
      id: {
        title: 'ID',
      },
      email: {
        title: 'Email',
      },
      fullName: {
        title: 'Full Name',
      },
      status: {
        title: 'Status',
      },
    },
  };

  source = new ServerDataSource(this._http, {
    dataKey: 'content',
    endPoint: this.service.baseUrl,
    pagerPageKey: 'page',
    totalKey: 'totalPages',
  });

  onSearch(query: string = '') {
    this.source.setFilter([
      {
        field: 'q',
        search: query,
      },
    ], false);
  }

  create() {
    this.dialogService.open(UserDialogComponent, {
      context: {
        isCreate: true,
        selectedItem: {},
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }

  onRowSelect(selectedRow) {
    this.dialogService.open(UserDialogComponent, {
      context: {
        isCreate: false,
        selectedItem: selectedRow.data,
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }
}
