import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ServerDataSource } from 'ng2-smart-table';
import { CategoryService } from './category.service';
import { NbDialogService } from '@nebular/theme';
import { CategoryDialogComponent } from './dialog/dialog.component';
import { environment } from '../../../environments/environment';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'ngx-category',
  templateUrl: 'category.component.html',
  styleUrls: ['category.component.scss'],
})
export class CategoryComponent {

  constructor(protected service: CategoryService,
    private dialogService: NbDialogService,
    private datePipe: DatePipe,
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
        title: 'Name',
      },
      status: {
        title: 'Status',
      },
      created: {
        title: 'Created Date',
        valuePrepareFunction: (date) => {
          const raw = new Date(date);
          return this.datePipe.transform(raw, 'yyyy-MM-dd HH:mm:ss');
        },
      },
      updated: {
        title: 'Last modified',
        valuePrepareFunction: (date) => {
          const raw = new Date(date);
          return this.datePipe.transform(raw, 'yyyy-MM-dd HH:mm:ss');
        },
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
    this.dialogService.open(CategoryDialogComponent, {
      context: {
        isCreate: true,
        selectedItem: {},
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }

  onRowSelect(selectedRow) {
    this.dialogService.open(CategoryDialogComponent, {
      context: {
        isCreate: false,
        selectedItem: selectedRow.data,
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }
}
