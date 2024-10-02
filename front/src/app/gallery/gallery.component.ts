import { Component, OnInit } from '@angular/core';
import {  TemplateRef } from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { GalleryServiceService } from './gallery-service.service';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.scss']
})
export class GalleryComponent implements OnInit {

  modalRef?: BsModalRef;

  constructor(private modalService: BsModalService,  private toastr: ToastrService, private RS: GalleryServiceService) {}



  ngOnInit(): void {
    this.getSlider();
    this.pagination.perPage = this.perPages[0];
    this.getList();
  }

  listOfImages = [
  //   'https://images.unsplash.com/photo-1656381836187-79f6d0ae2e57?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80',
  //   'https://images.unsplash.com/photo-1575413363724-351d954a2a4d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80',
  //   'https://images.unsplash.com/photo-1584709521634-a43d8bb202f7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1035&q=80',
  //   'https://images.unsplash.com/photo-1565089713183-8f7a8e43c206?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1yZWxhdGVkfDd8fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60',
  //   'https://images.unsplash.com/photo-1553152531-b98a2fc8d3bf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1631&q=80',
  //   'https://images.unsplash.com/photo-1531297484001-80022131f5a1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1120&q=80',
  //   'https://images.unsplash.com/photo-1610653216265-74079d187414?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1171&q=80',
  // 
]

  imageSource = '';

  showImage(template: TemplateRef<any>, e: any){
    this.modalRef = this.modalService.show(
      template,
      Object.assign({}, { class: 'modal-xl' })
    );

    var element = e.target;
    this.imageSource = element.src;

  }

  nextImage(e:any){
    var element: any = e.target;
    var imageSource = element.previousElementSibling;
    // console.log(imageSource);
    var source = imageSource!.src;
    // console.log(this.listOfImages.length)
    for (var i = 0; i<this.listOfImages.length; i++){
      if (source == this.listOfImages[i]){
        if (i == this.listOfImages.length -1){
          break;
        }
        this.imageSource = this.listOfImages[i+1]
      
      }
    }
  }

  previousImage(e:any){
    var element: any = e.target;
    var imageSource = element.nextElementSibling;
    var source = imageSource!.src;
    // console.log(this.listOfImages.length)
    for (var i = 0; i<this.listOfImages.length; i++){
      if (source == this.listOfImages[i]){
        if (i == 0){
          break;
        }
        this.imageSource = this.listOfImages[i-1]
      }
    }
  }

  slider:any;
   burl="http://dashboard.hmis.gov.np/";
  getSlider(){
  
    this.RS.getSlider().subscribe({
    
      next: (result:any) => {
           this.slider = result.data; 
           this.listOfImages = this.slider.map((item: { image: any; }) => this.burl + item.image);
          //  console.log(listOfImages);
         
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }



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
  formLayout: any;

  getList(pageno?: number | undefined) {
    const page = pageno || 1;
    this.RS.getList(this.pagination.perPage, page, this.searchTerm, this.column, this.isDesc).subscribe({
      next: (result: any) => {
        this.lists = result.data;
        for (let a=0; a<this.lists.length; a++){
          // console.log(this.lists[a].image) 
          // this.listOfImages.push(this.lists[a].image)
        }
        this.pagination.total = result.total;
        this.pagination.currentPage = result.currentPage;
      },
      error: err => {
        this.toastr.error(err.error, 'Error');
      }
    });
  }
}
