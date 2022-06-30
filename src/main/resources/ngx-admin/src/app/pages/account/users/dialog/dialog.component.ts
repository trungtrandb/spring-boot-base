import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NbAuthJWTToken, NbAuthService } from '@nebular/auth';
import { NbDialogRef, NbToastrService } from '@nebular/theme';
import { Observable, of } from 'rxjs';
import { KeyValueModel } from '../../../../@core/model/common.model';
import { RoleService } from '../../roles/role.service';
import { UserService } from '../user.service';

@Component({
  selector: 'ngx-edit',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
})
export class UserDialogComponent implements OnInit {
  selectedItem: any;
  isCreate: boolean = true;
  editableRole: boolean = true;
  form: FormGroup;
  min: any;
  disableSave: boolean = false;
  roleSelections: Observable<KeyValueModel[]>;
  roleIds: Number[];
  //   options: KeyValueModel[];
  //   permissions: Observable<KeyValueModel[]>;

  constructor(
    protected dialogRef: NbDialogRef<UserDialogComponent>,
    private service: UserService,
    private roleService: RoleService,
    private fb: FormBuilder,
    private authService: NbAuthService,
    private toastService: NbToastrService,
  ) { }

  ngOnInit(): void {
    this.authService.onTokenChange()
      .subscribe((token: NbAuthJWTToken) => {
        if (token.isValid()) {
          this.editableRole = token.getPayload()['id'] !== this.selectedItem.id;
        }
      });
    this.initForm();
    this.initRoleSelect();
  }

  close() {
    this.dialogRef.close(false);
  }

  save() {
    this.disableSave = true;
    if (!this.editableRole) {
      this.form.controls['roles'].setValue(this.roleIds);
    }
    this.service.create(this.form.value).subscribe((res) => {
      if (res) this.dialogRef.close(true);
    });
    this.disableSave = false;
  }

  delete() {
    this.service.delete(this.selectedItem?.id).subscribe((data) => {
      this.dialogRef.close(true);
    });
  }

  initRoleSelect() {
    this.roleService.getAll().subscribe((res) => {
      const mapValue = res.content.map((item) => {
        return <KeyValueModel>{
          value: item.id,
          label: item.name,
        };
      });
      this.roleSelections = of(mapValue);
    });
  }


  initForm() {
    this.form = this.fb.group({
      id: [],
      fullName: [],
      email: [],
      password: [],
      address: [],
      roles: [],
      gender: [],
    });
    if (this.selectedItem.id) {
      this.disableSave = true;
      this.loadUserData(this.selectedItem.id);
    }
  }

  loadUserData(id: Number) {
    this.service.getById(id).subscribe(res => {
      this.form.controls['id'].setValue(res.id);
      this.form.controls['fullName'].setValue(res.fullName);
      this.form.controls['email'].setValue(res.email);
      this.form.controls['address'].setValue(res.address);
      this.form.controls['gender'].setValue(res.gender?.toString());
      this.roleIds = res.roleIds,
      this.disableSave = false;
    });
  }
}

