import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { InfographicsServiceService } from './infographics-service.service';
import { ImageCroppedEvent, LoadedImage } from 'ngx-image-cropper';

@Component({
  selector: 'app-infographics',
  templateUrl: './infographics.component.html',
  styleUrls: ['./infographics.component.scss']
})
export class InfographicsComponent implements OnInit {

  infographicsForm: FormGroup

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

  constructor(private RS: InfographicsServiceService, private toastr: ToastrService, private fb: FormBuilder) {

    this.formLayout = {
      id: [],
      title: ['', Validators.required],
      subtitle: [''],
      description: [''],
      image: ['', Validators.required],
      time: ['10', Validators.required],
      status: ['', Validators.required]
    }

    this.infographicsForm = fb.group(this.formLayout)

    this.srchForm = new FormGroup({
      entries: new FormControl('10'),
      srch_term: new FormControl(''),

    })

  }

  imageChangedEvent: any = '';
  croppedImage: any = '';

  fileChangeEvent(event: any): void {
    this.imageChangedEvent = event;
  }
  imageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;
  }

  myVideos: any = [];
  isUploading: boolean = false;
  async handleFileInput(e: Event) {

    const target = e.target as HTMLInputElement;
    const files = target.files as FileList;
    const file = files[0];

    if (file.type! == "image/png" || file.type! == 'video/mp4' || file.type! == 'image/jpeg' || file.type! == 'image/jpg') {
            
      if (file.size <= 200000000) { 
        if (file.type == "image/png" || file.type == 'image/jpeg' || file.type == 'image/jpg'){
          if (file.size >= 200000000) {  //4 MB (UNIT in Bytes)
            this.toastr.error("Image Uploaded shouldn't be more than 200 mb", "Error");
            return;
          }
          else{
            this.imageChangedEvent = event;
            document.getElementById('imageDisplay')?.classList.remove('d-none');
            document.getElementById('imgSelect')?.classList.remove('d-none');
            if (file) {
              // if(file.type=="image/jpeg")
              this.isUploading = true;
              
              // if(file.type == 'video/mp4'){

              // }
              const resizedImage =  this.resizeImage(file, 1280, 650);
              const formData: FormData = new FormData();
              formData.append('file', await resizedImage, file.name);
             
              this.RS.uploadFile(formData).subscribe({
                next: (data: any) => {
                  // console.log(data);
                  if (data.status) {
                    this.infographicsForm.patchValue({ image: data.path });
                    this.isUploading = false;
                  }
                }, error: err => {
                  this.isUploading = false;
                  // console.log(err);
                }
              })
            }
          }
        }

        else{

          if (file) {
            // var vlength = document.getElementById('mfile') as HTMLMediaElement
            // console.log(vlength!.duration)
            this.isUploading = true;
            // const resizedImage =  this.resizeImage(file, 1280, 768);
            const formData: FormData = new FormData();
            formData.append('file', file, file.name);
          
            this.RS.uploadFile(formData).subscribe({
              next: (data: any) => {
                if (data.status) {
                  this.infographicsForm.patchValue({ image: data.path });
                  this.isUploading = false;
                }
              }, error: err => {
                this.isUploading = false;
                // console.log(err);
              }
            })
          }
          
        }

      }
      else {
        const f = document.getElementById('mfile') as HTMLInputElement
        f!.value = "";
        this.toastr.error("Video should not be more than 200 MB", "Error")
        
      }
    }
    else {
      const f = document.getElementById('mfile') as HTMLInputElement
      f!.value = "";
      this.toastr.error("Please Upload proper file format", "Error")
    }
    // console.log('size', file.size);
  }
  ctx:any;
  resizeImage(file: File, maxWidth: number, maxHeight: number): Promise<Blob> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (event: any) => {
        const img = new Image();
        img.src = event.target.result;
        img.onload = () => {
          const canvas = document.createElement('canvas');
          let width = img.width;
          let height = img.height;

          if (width > height) {
            if (width > maxWidth) {
              height *= maxWidth / width;
              width = maxWidth;
            }
          } else {
            if (height > maxHeight) {
              width *= maxHeight / height;
              height = maxHeight;
            }
          }

          canvas.width = width;
          canvas.height = height;

           this.ctx = canvas.getContext('2d');
          this.ctx.drawImage(img, 0, 0, width, height);
          canvas.toBlob((blob) => {
            if (blob) {
              resolve(blob);
            } else {
              reject(new Error('Failed to convert canvas to blob'));
            }
          }, file.type);
        
        };
      };
      reader.onerror = (error) => reject(error);
      reader.readAsDataURL(file);
    });
  }

  uploadFile(){

  }

  ngOnInit(): void {
    this.pagination.perPage = this.perPages[0];
    this.getList();
  }

  

  infographicsFormSubmit() {
    if (this.infographicsForm.valid) {
      this.model = this.infographicsForm.value;
      document.getElementById('imageDisplay')?.classList.add('d-none');
      document.getElementById('imgSelect')?.classList.add('d-none')
      this.createItem(this.infographicsForm.value.id);
    } else {
      // Object.keys(this.infographicsForm.controls).forEach(field => {
      //   const singleFormControl = this.infographicsForm.get(field);
      //   singleFormControl?.markAsTouched({onlySelf: true});
      // });
      this.toastr.error('Please fill all the required* fields', "Error");
    }
  }

  createItem(id = null) {
    let upd = this.model;
    if (id != "" && id != null) {
      this.RS.update(id, upd).subscribe({
        next: (result: any) => {
          this.toastr.success('Item Successfully Updated!', 'Success');
          this.infographicsForm = this.fb.group(this.formLayout)
          this.getList();
        }, error: err => {
          this.toastr.error(err.error.message, 'Error');
        }
      });
    } else {
      this.RS.create(upd).subscribe({
        next: (result: any) => {
          this.toastr.success('Item Successfully Saved!', 'Success');
          this.infographicsForm = this.fb.group(this.formLayout)
          this.getList();
        }, error: err => {
          this.toastr.error(err.error.message, 'Error');
        }
      });
    }

  }
  getList(pageno?: number | undefined) {
    const page = pageno || 1;
    this.RS.getList(this.pagination.perPage, page, this.searchTerm, this.column, this.isDesc).subscribe({
      next: (result: any) => {
        this.lists = result.data;
        this.pagination.total = result.total;
        this.pagination.currentPage = result.currentPage;
        ///console.log(result);
      },
      error: err => {
        this.toastr.error(err.error, 'Error');
      }
    });
  }

  resetForm() {
    const f = document.getElementById('mfile') as HTMLInputElement
    f!.value = "";

    document.getElementById('imageDisplay')?.classList.add('d-none'); 
    document.getElementById('imgSelect')?.classList.add('d-none'); 

    this.infographicsForm = this.fb.group(this.formLayout);
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
    this.pagination.perPage = this.srchForm.value.entries;
    this.searchTerm = this.srchForm.value.srch_term;
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
      next: (result: any) => {
        this.model = result;
        // document.getElementById('imageDisplay')?.classList.remove('d-none'); 
        // document.getElementById('imgSelect')?.classList.remove('d-none'); 

        this.infographicsForm.patchValue(result);


      },
      error: (err: any) => {
        this.toastr.error(err.error, 'Error');
      }
    }
    );
  }

  deleteItem(id: any) {
    if (window.confirm('Are sure you want to delete this item?')) {
      this.RS.remove(id).subscribe({
        next: (result: any) => {
          this.toastr.success('Item Successfully Deleted!', 'Success');
          this.getList();
        }, error: (error: { error: any; }) => {
          this.toastr.error(error.error, 'Error');
        }
      });
    }

  }

}
