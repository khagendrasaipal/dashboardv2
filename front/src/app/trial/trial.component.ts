import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'app-trial',
  templateUrl: './trial.component.html',
  styleUrls: ['./trial.component.scss']
})
export class TrialComponent implements OnInit {

  constructor(private ls: LoginService) { }

  ngOnInit(): void {
    this.getOrglist();
  }
  orglist:any;
  getOrglist(){
    this.ls.getorganizationList().subscribe({
      next: (result:any) => {
          // console.log(result.data[0]);
          this.orglist=result.data;
          // console.log(this.orglist);
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

}
