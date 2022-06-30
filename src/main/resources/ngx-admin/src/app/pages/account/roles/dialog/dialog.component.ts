import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NbDialogRef } from '@nebular/theme';
import { Observable, of } from 'rxjs';
import { KeyValueModel } from '../../../../@core/model/common.model';
import { RoleService } from '../role.service';

@Component({
  selector: 'ngx-edit',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
})
export class RoleDialogComponent implements OnInit {
  selectedItem: any;
  isCreate: boolean = true;
  form: FormGroup;
  min: any;
  disableSave: boolean = false;
  selectedIds:  Number[] = [];
  privilegeSelections: Observable<KeyValueModel[]>;

  constructor(
    protected dialogRef: NbDialogRef<RoleDialogComponent>,
    private service: RoleService,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.initPrivilegeSelect();
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
    if (this.selectedItem?.privileges) {
      this.selectedItem.privileges.map((item) => {
        this.selectedIds.push(item.id);
      });
    }
    this.form = this.fb.group({
      id: [this.selectedItem?.id],
      name: [ this.selectedItem?.name],
      privilegeIds: [this.selectedIds],
    });
  }

  initPrivilegeSelect() {
    this.service.getAllPrivilege().subscribe((res) => {
      const mapValue = res.map((item) => {
        return <KeyValueModel>{
          value: item.id,
          label: item.name,
        };
      });
      this.privilegeSelections = of(mapValue);
    });
  }
}
