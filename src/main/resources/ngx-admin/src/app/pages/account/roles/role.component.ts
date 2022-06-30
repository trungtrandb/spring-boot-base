import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ServerDataSource } from 'ng2-smart-table';
import { RoleService } from './role.service';
import { NbDialogService } from '@nebular/theme';
import { RoleDialogComponent } from './dialog/dialog.component';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'ngx-role',
  templateUrl: 'role.component.html',
  styleUrls: ['role.component.scss'],
})
export class RoleComponent {

  constructor(protected service: RoleService,
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
      name: {
        title: 'Role Name',
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
    this.dialogService.open(RoleDialogComponent, {
      context: {
        isCreate: true,
        selectedItem: {},
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }

  onRowSelect(selectedRow) {
    this.dialogService.open(RoleDialogComponent, {
      context: {
        isCreate: false,
        selectedItem: selectedRow.data,
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }
}
