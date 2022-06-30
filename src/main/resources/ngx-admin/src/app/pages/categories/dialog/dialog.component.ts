import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NbDialogRef } from '@nebular/theme';
import { Observable, of } from 'rxjs';
import { KeyValueModel } from '../../../@core/model/common.model';
import { CategoryService } from '../category.service';

@Component({
  selector: 'ngx-edit',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
})
export class CategoryDialogComponent implements OnInit {
  selectedItem: any;
  isCreate: boolean = true;
  form: FormGroup;
  min: any;
  disableSave: boolean = false;
  privilegeSelections: Observable<KeyValueModel[]>;

  constructor(
    protected dialogRef: NbDialogRef<CategoryDialogComponent>,
    private service: CategoryService,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  close() {
    this.dialogRef.close(false);
  }

  save() {
    this.disableSave = true;
    this.service.create(this.form.value).subscribe((res) => {
      if (res) this.dialogRef.close(true);
    });
    this.disableSave = false;
  }

  delete() {
      this.service.delete(this.selectedItem?.id).subscribe((res) => {
        if (res) this.dialogRef.close(true);
   });
  }

  initForm() {
    this.form = this.fb.group({
      id: [this.selectedItem?.id],
      name: [ this.selectedItem?.name],
      status: [this.selectedItem?.status?.toString()],
    });
  }
}
