import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ServerDataSource } from 'ng2-smart-table';
import { PostService } from './post.service';
import { NbDialogService } from '@nebular/theme';
import { PostDialogComponent } from './dialog/dialog.component';
import { environment } from '../../../environments/environment';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'ngx-post',
  templateUrl: 'post.component.html',
  styleUrls: ['post.component.scss'],
})
export class PostComponent {

  constructor(protected service: PostService,
    private dialogService: NbDialogService,
    private datePipe: DatePipe,
    private _http: HttpClient) {
  }

  source = new ServerDataSource(this._http, {
    dataKey: 'content',
    endPoint: this.service.baseUrl,
    pagerPageKey: 'page',
    totalKey: 'totalPages',
  });

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
      categoryName: {
        title: 'Category',
      },
      slug: {
        title: 'Slug',
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
        title: 'Last Modified',
        valuePrepareFunction: (date) => {
          const raw = new Date(date);
          return this.datePipe.transform(raw, 'yyyy-MM-dd HH:mm:ss');
        },
      },
    },
  };

  onSearch(query: string = '') {
    this.source.setFilter([
      {
        field: 'q',
        search: query,
      },
    ], false);
  }

  create() {
    this.dialogService.open(PostDialogComponent, {
      context: {
        isCreate: true,
        selectedItem: {},
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }

  onRowSelect(selectedRow) {
    this.dialogService.open(PostDialogComponent, {
      context: {
        isCreate: false,
        selectedItem: selectedRow.data,
      },
    }).onClose.subscribe(result => {
      if (result) this.source.refresh();
    });
  }
}
