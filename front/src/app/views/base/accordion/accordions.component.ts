import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FaqService } from 'src/app/faq/faq.service';

@Component({
  selector: 'app-accordions',
  templateUrl: './accordions.component.html',
  styleUrls: ['./accordions.component.scss']
})
export class AccordionsComponent implements OnInit{

  items = [1, 2, 3, 4];

  constructor(
    private sanitizer: DomSanitizer,private RS: FaqService,
  ) { }
  ngOnInit(): void {
    this.getFaqs();
    throw new Error('Method not implemented.');
  }
faqs:any;
  getFaqs(){
    this.RS.getFaqs().subscribe({
      next: (result:any) => {
           this.faqs = result;
           console.log(this.faqs);
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getAccordionBodyText(value: string) {
    const textSample = `
      <strong>This is the <mark>#${value}</mark> item accordion body.</strong> It is hidden by
      default, until the collapse plugin adds the appropriate classes that we use to
      style each element. These classes control the overall appearance, as well as
      the showing and hiding via CSS transitions. You can modify any of this with
      custom CSS or overriding our default variables. It&#39;s also worth noting
      that just about any HTML can go within the <code>.accordion-body</code>,
      though the transition does limit overflow.
    `;
    return this.sanitizer.bypassSecurityTrustHtml(textSample);
  }
}
