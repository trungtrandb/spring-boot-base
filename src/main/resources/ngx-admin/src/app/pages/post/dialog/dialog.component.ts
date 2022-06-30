import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NbDialogRef } from '@nebular/theme';
import { Observable, of } from 'rxjs';
import { KeyValueModel } from '../../../@core/model/common.model';
import { Post } from '../../../@core/model/post.model';
import { PostService } from '../post.service';

@Component({
  selector: 'ngx-edit',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
})
export class PostDialogComponent implements OnInit {
  selectedItem: Post;
  isCreate: boolean = true;
  form: FormGroup;
  disableSave: boolean = false;
  categorySelections: Observable<KeyValueModel[]>;

  constructor(
    protected dialogRef: NbDialogRef<PostDialogComponent>,
    private service: PostService,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.initCategorySelect();
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
      slug: [ this.selectedItem?.slug],
      categoryId: [this.selectedItem?.categoryId],
      content: [this.selectedItem?.content],
      status: [this.selectedItem?.status?.toString() ?? 'ACTIVE'],
    });

    this.form.controls['name'].valueChanges.subscribe(value => {
      this.form.controls['slug'].setValue(value?.replace(/[^\w]/gi, '-').toLowerCase());
    });
  }

  initCategorySelect() {
    this.service.getAllCategory().subscribe((res) => {
      const mapValue = res.content.map((item) => {
        return <KeyValueModel>{
          value: item.id,
          label: item.name,
        };
      });
      this.categorySelections = of(mapValue);
    });
  }

  onContentChange(val: string) {
    this.form.controls['content'].setValue(val);
  }
}
