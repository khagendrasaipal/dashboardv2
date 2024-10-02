import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder, NgModel } from '@angular/forms';
import { OrganizationService } from './organization.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {

  organizationForm: FormGroup;
  srchForm: FormGroup;

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
  adminlvls: any;
  federals: any;
  parents: any;
  orglevels: any;
  provinces: any;
  districts: any;
  palikas: any;
  wards = new Array(33);
  searchTerm: string = '';
  column: string = '';
  isDesc: boolean = false;
  adminstr: any;


  formLayout: any;
  slct: any;

  constructor(private toastr: ToastrService, private RS: OrganizationService, private fb: FormBuilder) {

    this.formLayout = {
      adm_level: [" ", Validators.required],
      adm_id: ['', Validators.required],
      parent: [''],
      name: ['', Validators.required],
      namenp: ['', Validators.required],
      code: [''],
      orgidint:[''],
      uid:['']

    };

  //   this.organizationForm = fb.group({
  //     'adminlevel': ['0',Validators.required],
  //     'orgidint': ['']
  // });


    this.organizationForm = fb.group(this.formLayout);

    this.srchForm = fb.group({
      'entries': new FormControl('10'),
      'srch_term': new FormControl(''),

    })
  }
  getAdmin(lid:any){
    this.RS.getAdmins(lid).subscribe({
      next: (result:any) => {
           this.adminstr = result.data; 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getParent(aids: any){
    const aid = this.organizationForm.get('adm_id')!.value
    this.RS.getParent(aid).subscribe({
      next: (result:any) => {
           this.parents = result.data; 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  ngOnInit(): void {
    this.pagination.perPage = this.perPages[0];
    this.getList();
    
  }

 

  resetForm() {
    this.organizationForm = this.fb.group(this.formLayout);
    
  }

  getDistricts() {
    this.RS.getDistricts(this.organizationForm.value.provinceid).subscribe(
      {
        next: (v) => {
          console.log(v),
          this.districts = v},
        error: (e) =>{
          console.error(e),
          this.toastr.error(e.error, 'Error');
        } ,
        complete: () => {
          // this.districts = result.data;
        }
    }
     
    );
  }



  



  organizationFormSubmit() {
    if (this.organizationForm.valid) {
      this.model = this.organizationForm.value;
      this.createItem(this.organizationForm.value.orgidint);
    } else {
      // Object.keys(this.organizationForm.controls).forEach(field => {
      //   const singleFormControl = this.organizationForm.get(field);
      //   singleFormControl!.markAsTouched({ onlySelf: true });
      // });
      this.toastr.error("Please fill all the required* fields", "Error")
    }
  }

  createItem(id = null) {

    let upd = this.model;
    if (id != "" && id != null) {

      this.RS.update(id, upd).subscribe({
        next: (result :any) => {
        this.toastr.success('Item Successfully Updated!', 'Success');
        this.organizationForm = this.fb.group(this.formLayout)
        this.getList();
      }, error :err=> {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    } else {
      this.RS.create(upd).subscribe({
        next:(result:any) => {

        this.toastr.success('Item Successfully Saved!', 'Success');
        
        this.organizationForm = this.fb.group(this.formLayout)
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

  search() {
    this.pagination.perPage = this.srchForm.value.entries;
    this.searchTerm = this.srchForm.value.srch_term;
    this.getList();
  }

  paginatedData($event: { page: number | undefined; }) {
    this.getList($event.page);
  }

  changePerPage(perPage: number) {
    this.pagination.perPage = perPage;
    this.pagination.currentPage = 1;
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
        this.organizationForm.patchValue(result);
        this.getAdmin(this.organizationForm.value.adm_level);
        this.organizationForm.patchValue(result);
        this.getParent(this.organizationForm.value.adm_id);
        this.organizationForm.patchValue(result);
       

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
