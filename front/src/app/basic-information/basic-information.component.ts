import { Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BasicInformationService } from './basic-information.service';

// import { Editor } from 'ngx-editor';


@Component({
  selector: 'app-basic-information',
  templateUrl: './basic-information.component.html',
  styleUrls: ['./basic-information.component.scss']
})
export class BasicInformationComponent implements OnInit {
  bascicInformationForm: FormGroup
  // editor!: Editor;
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

  name = 'ng2-ckeditor';    
  ckeConfig={
    allowedContent: false,
    forcePasteAsPlainText: true,
    font_names: 'Arial;Times New Roman;Verdana',
    toolbarGroups: [
      { name: 'document', groups: ['mode', 'document', 'doctools'] },
      { name: 'clipboard', groups: ['clipboard', 'undo'] },
      { name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing'] },
      { name: 'forms', groups: ['forms'] },
      '/',
      { name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
      { name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },
      { name: 'links', groups: ['links'] },
      { name: 'insert', groups: ['insert'] },
      '/',
      { name: 'styles', groups: ['styles'] },
      { name: 'colors', groups: ['colors'] },
      { name: 'tools', groups: ['tools'] },
      { name: 'others', groups: ['others'] },
      { name: 'about', groups: ['about'] }
    ],
    // removeButtons: 'Source,Save,NewPage,Preview,Print,Templates,Cut,Copy,Paste,PasteText,PasteFromWord,Undo,Redo,Find,Replace,SelectAll,Scayt,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Strike,Subscript,Superscript,CopyFormatting,RemoveFormat,Outdent,Indent,CreateDiv,Blockquote,BidiLtr,BidiRtl,Language,Unlink,Anchor,Image,Flash,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,Maximize,ShowBlocks,About'
  };
   mycontent: string | undefined;    
  log: string | undefined   
  @ViewChild('PageContent') PageContent: any;  

  constructor(private RS: BasicInformationService, private toastr: ToastrService, private fb: FormBuilder,private renderer: Renderer2, private el: ElementRef) { 
    
    this.formLayout = {
      id:[],
      title: [''],
      type: ['',Validators.required],
      content: ['', Validators.required],
      status: ['', Validators.required]
    }
    
    this.bascicInformationForm =fb.group(this.formLayout)

    this.srchForm = new FormGroup({
      entries: new FormControl('10'),
      srch_term: new FormControl(''),
      
    })

  }

  ngOnInit(): void {
    // this.editor = new Editor();
    this.pagination.perPage = this.perPages[0];
    this.getList();
    $("#cke_notifications_area_editor1").hide();
    
  }

  ngAfterViewInit() {
    $("#cke_notifications_area_editor1").hide();
    setTimeout(() => {
    const notificationArea = this.el.nativeElement.querySelector('.cke_notifications_area');
    if (notificationArea) {
      this.renderer.setStyle(notificationArea, 'display', 'none');
    }
    $("#cke_notifications_area_editor1").hide();
    console.log(notificationArea);
  }, 500);
  $("#cke_notifications_area_editor1").hide();
  }

  bascicInformationFormSubmit(){
    if (this.bascicInformationForm.valid) {
      this.model = this.bascicInformationForm.value;
      this.createItem(this.bascicInformationForm.value.id);
    } else {
      Object.keys(this.bascicInformationForm.controls).forEach(field => {
        const singleFormControl = this.bascicInformationForm.get(field);
        singleFormControl?.markAsTouched({onlySelf: true});
      });
    }
  }

  getTitle(type:any){
  
    if(type=="Additional Cards"){
      $("#titl").show();
    }else{
      $("#titl").hide();
    }
   
    
  }

  changeTextArea(){

    // var fancytextarea = document.getElementById('fancytextarea')
    // var non_fancyTextArea = document.getElementById('textarea')
    
    //   var ele = document.getElementById('selectDropdown') as HTMLInputElement;

    //   if (ele.value == 'Introduction' || ele.value =='Important Information about LLG' || ele.value =='Tips (सुझावहरू)' || ele.value =='Ward Contacts' || ele.value=='Additional Cards'){
        
    //     fancytextarea?.classList.remove('d-none');
    //     non_fancyTextArea?.classList.add('d-none');
      
    //   }
    //   else {
    //     fancytextarea?.classList.add('d-none');
    //     non_fancyTextArea?.classList.remove('d-none');
    //   }
      
  }

 
  // createItem(id = null) {

  //   let upd = this.model;
  //   if (id != "" && id != null) {
  //     this.RS.update(id, upd).subscribe(result => {
  //       this.toastr.success('Item Successfully Updated!', 'Success');
  //       //this.bascicInformationForm.reset();
  //       this.bascicInformationForm =this.fb.group(this.formLayout);
  //       this.getList();
  //     }, error => {
  //       this.toastr.error(error.error, 'Error');
  //     });
  //   } else {
  //     this.RS.create(upd).subscribe(result => {
  //       this.toastr.success('Item Successfully Saved!', 'Success');
  //       //this.bascicInformationForm.reset();
  //       this.bascicInformationForm =this.fb.group(this.formLayout);
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
        this.bascicInformationForm = this.fb.group(this.formLayout)
        this.getList();
      }, error :err=> {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    } else {
      this.RS.create(upd).subscribe({
        next:(result:any) => {
        this.toastr.success('Item Successfully Saved!', 'Success');
        this.bascicInformationForm = this.fb.group(this.formLayout)
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
    this.bascicInformationForm =this.fb.group(this.formLayout);
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
        // this.changeTextArea();
        // this.changeTextwhileEdit(result.type)
        this.bascicInformationForm.patchValue(result);
      },
      error:(err: any) => {
        this.toastr.error(err.error, 'Error');
      }
    }
    );
  }

  changeTextwhileEdit(ele: any){

    // console.log(ele)

    var fancytextarea = document.getElementById('fancytextarea')
      var non_fancyTextArea = document.getElementById('textarea')
      
      if (ele == 'Introduction' || ele =='Important Information about LLG' || ele =='Tips (सुझावहरू)' || ele =='Ward Contacts' || ele=='Additional Cards'){
        
        fancytextarea?.classList.remove('d-none');
        non_fancyTextArea?.classList.add('d-none');
      
      }
      else {
        fancytextarea?.classList.add('d-none');
        non_fancyTextArea?.classList.remove('d-none');
      }

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
