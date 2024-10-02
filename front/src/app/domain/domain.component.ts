import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { DomainService } from './domain.service';

@Component({
  selector: 'app-domain',
  templateUrl: './domain.component.html',
  styleUrls: ['./domain.component.scss']
})
export class DomainComponent implements OnInit {

  cForm: FormGroup

  model: any = {};
  disabled = false;
  error = '';
  lists: any;
  perPages = [10, 20, 50, 100];
  pagination = {
    total: 0,
    currentPage: 0,
    perPage: 0
  };
  searchTerm: string = '';
  column: string = '';
  isDesc: boolean = false;

  srchForm: FormGroup;
  formLayout: any;

  constructor(private RS: DomainService, private toastr: ToastrService, private fb: FormBuilder) { 
    
    this.formLayout = {
      id:[],
      name: ['',Validators.required],
      code: ['']
    }
    
    this.cForm =fb.group(this.formLayout)

    this.srchForm = new FormGroup({
      entries: new FormControl('10'),
      srch_term: new FormControl(''),
      
    })

  }

  ngOnInit(): void {
    this.pagination.perPage = this.perPages[0];
    this.getList();
  }

  cFormSubmit(){
    if (this.cForm.valid) {
      this.model = this.cForm.value;
      this.createItem(this.cForm.value.id);
    } else {
      Object.keys(this.cForm.controls).forEach(field => {
        const singleFormControl = this.cForm.get(field);
        singleFormControl?.markAsTouched({onlySelf: true});
      });
    }
  }

 
  // createItem(id = null) {

  //   let upd = this.model;
  //   if (id != "" && id != null) {
  //     this.RS.update(id, upd).subscribe(result => {
  //       this.toastr.success('Item Successfully Updated!', 'Success');
  //       //this.cForm.reset();
  //       this.cForm =this.fb.group(this.formLayout);
  //       this.getList();
  //     }, error => {
  //       this.toastr.error(error.error, 'Error');
  //     });
  //   } else {
  //     this.RS.create(upd).subscribe(result => {
  //       this.toastr.success('Item Successfully Saved!', 'Success');
  //       //this.cForm.reset();
  //       this.cForm =this.fb.group(this.formLayout);
  //       this.getList();
  //     }, error => {
  //       this.toastr.error(error.error, 'Error');
  //     });
  //   }

  // }

  createItem(id = null) {

    let upd = this.model;
    if (id != "" && id != null) {

      this.RS.update(id, upd).subscribe({
        next: (result :any) => {
        this.toastr.success('Item Successfully Updated!', 'Success');
        this.cForm = this.fb.group(this.formLayout)
        this.getList();
      }, error :err=> {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    } else {
      this.RS.create(upd).subscribe({
        next:(result:any) => {
        this.toastr.success('Item Successfully Saved!', 'Success');
        this.cForm = this.fb.group(this.formLayout)
        this.getList();
      }, error:err => {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    }

  }
  getList(pageno?: number | undefined) {
    const page = pageno || 1;
    this.RS.getList(this.pagination.perPage, page, this.searchTerm, this.column, this.isDesc).subscribe({
      next:(result: any) => {
        this.lists = result.data;
        this.pagination.total = result.total;
        this.pagination.currentPage = result.currentPage;
        ///console.log(result);
      },
      error:err => {
        this.toastr.error(err.error, 'Error');
      }
    });
  }

  resetForm(){
    this.cForm =this.fb.group(this.formLayout);
  }

  

  paginatedData($event: { page: number | undefined; }) {
    this.getList($event.page);
  }

  changePerPage(perPage: number) {
    this.pagination.perPage = perPage;
    this.pagination.currentPage = 1;
    this.getList();
  }

  search() {
    this.pagination.perPage=this.srchForm.value.entries;
    this.searchTerm=this.srchForm.value.srch_term;
    this.getList();

  }

  resetFilters() {
    this.isDesc = false;
    this.column = '';
    this.searchTerm = '';
    this.pagination.currentPage = 1;
    this.getList();
  }

  getUpdateItem(id: any) {
   
    this.RS.getEdit(id).subscribe({
      next:(result: any) => {
        this.model = result;
        this.cForm.patchValue(result);
       

      },
      error:(err: any) => {
        this.toastr.error(err.error, 'Error');
      }
    }
    );
  }

  deleteItem(id: any) {
    if (window.confirm('Are sure you want to delete this item?')) {
      this.RS.remove(id).subscribe({
        next:(result: any) => {
        this.toastr.success('Item Successfully Deleted!', 'Success');
        this.getList();
      }, error:(error: { error: any; }) => {
        this.toastr.error(error.error, 'Error');
      }});
    }
  
  }
}
