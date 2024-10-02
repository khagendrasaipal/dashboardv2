import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup,NgModel } from '@angular/forms';

import { DashboardChartsData, IChartProps } from './dashboard-charts-data';
import { ToastrService } from 'ngx-toastr';
import { OrganizationService } from 'src/app/organization/organization.service';
import { DboardService } from 'src/app/dboard/dboard.service';
import { LoginService } from 'src/app/login/login.service';
import { ChartjsModule } from '@coreui/angular-chartjs';
import { Chart } from 'angular-highcharts';
import * as Highcharts from 'highcharts';

interface IUser {
  name: string;
  state: string;
  registered: string;
  country: string;
  usage: number;
  period: string;
  payment: string;
  activity: string;
  avatar: string;
  status: string;
  color: string;
}

@Component({
  templateUrl: 'dashboard.component.html',
  styleUrls: ['dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  lists: any;
  perPages = [10, 20, 50, 100];
  pagination = {
    total: 0,
    currentPage: 0,
    perPage: 0
  };
  isLoading = false;
  highchart:any;
  burl="http://dashboard.hmis.gov.np";
  // isActive: boolean = false;
  srchForm: FormGroup;
  searchTerm: string = '';
  column: string = '';
  isDesc: boolean = false;
  constructor(private ls:LoginService,private DS: DboardService,private chartsData: DashboardChartsData,private toastr: ToastrService, private RS: OrganizationService,private fb: FormBuilder) {
    this.srchForm = fb.group({
      'entries': new FormControl('10'),
      'srch_term': new FormControl(''),
      'mindicator': new FormControl(),

    })
  }

  public users: IUser[] = [
    {
      name: 'Yiorgos Avraamu',
      state: 'New',
      registered: 'Jan 1, 2021',
      country: 'Us',
      usage: 50,
      period: 'Jun 11, 2021 - Jul 10, 2021',
      payment: 'Mastercard',
      activity: '10 sec ago',
      avatar: './assets/img/avatars/1.jpg',
      status: 'success',
      color: 'success'
    },
    {
      name: 'Avram Tarasios',
      state: 'Recurring ',
      registered: 'Jan 1, 2021',
      country: 'Br',
      usage: 10,
      period: 'Jun 11, 2021 - Jul 10, 2021',
      payment: 'Visa',
      activity: '5 minutes ago',
      avatar: './assets/img/avatars/2.jpg',
      status: 'danger',
      color: 'info'
    },
    {
      name: 'Quintin Ed',
      state: 'New',
      registered: 'Jan 1, 2021',
      country: 'In',
      usage: 74,
      period: 'Jun 11, 2021 - Jul 10, 2021',
      payment: 'Stripe',
      activity: '1 hour ago',
      avatar: './assets/img/avatars/3.jpg',
      status: 'warning',
      color: 'warning'
    },
    {
      name: 'Enéas Kwadwo',
      state: 'Sleep',
      registered: 'Jan 1, 2021',
      country: 'Fr',
      usage: 98,
      period: 'Jun 11, 2021 - Jul 10, 2021',
      payment: 'Paypal',
      activity: 'Last month',
      avatar: './assets/img/avatars/4.jpg',
      status: 'secondary',
      color: 'danger'
    },
    {
      name: 'Agapetus Tadeáš',
      state: 'New',
      registered: 'Jan 1, 2021',
      country: 'Es',
      usage: 22,
      period: 'Jun 11, 2021 - Jul 10, 2021',
      payment: 'ApplePay',
      activity: 'Last week',
      avatar: './assets/img/avatars/5.jpg',
      status: 'success',
      color: 'primary'
    },
    {
      name: 'Friderik Dávid',
      state: 'New',
      registered: 'Jan 1, 2021',
      country: 'Pl',
      usage: 43,
      period: 'Jun 11, 2021 - Jul 10, 2021',
      payment: 'Amex',
      activity: 'Yesterday',
      avatar: './assets/img/avatars/6.jpg',
      status: 'info',
      color: 'dark'
    }
  ];
  public mainChart: IChartProps = {};
  public chart: Array<IChartProps> = [];
  public trafficRadioGroup = new FormGroup({
    trafficRadio: new FormControl('Month')
  });

  ngOnInit(): void {
    this.ls.removeUserData();
    
    // this.open=false;
    // this.initCharts();
    this.pagination.perPage = this.perPages[0];
    this.getList();
    this.getIndicators(1);
    this.getHfCount();
    this.getCovidCount();
    this.getfy();
    this.getSlider();
    
  }

  onItemChange($event: any): void {
    console.log('Carousel onItemChange', $event);
  }
  indicators:any;
  getIndicators(pid:any){
  
    this.DS.getIndicators(pid).subscribe({
      next: (result:any) => {
           this.indicators = result.data; 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }
slider:any;
  getSlider(){
  
    this.DS.getSlider().subscribe({
      next: (result:any) => {
           this.slider = result.data; 
           this.showSlide();
           this.startSlide();
          //  console.log(this.slider);
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  fy:any;
  getfy(){
  
    this.DS.getfy().subscribe({
      next: (result:any) => {
           this.fy = result.data[0].name; 
          //  console.log(this.fy);
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }
hfs:any;
total=0;
gov=0;
nongov=0;
  getHfCount(){
  
    this.DS.getHfCount().subscribe({
      next: (result:any) => {
           this.total=result[0][0];
           this.gov=result[1][0];
           this.nongov=result[2][0];
       },
       error : err => {
         this.toastr.error("Unable to fetch data from NHFR", 'Error');
       }
       
     }
     );
  }
  imgShow=true;
  getInd(){
    this.chartshow=false;
    this.imgShow=true;
  }
covid=0;
  getCovidCount(){
  
    this.DS.getCovidCount().subscribe({
      next: (result:any) => {
           this.covid=result[0];
        
       },
       error : err => {
         this.toastr.error("Unable to fetch data from NHFR", 'Error');
       }
       
     }
     );
  }
  activeStates: { [key: number]: boolean } = {};
  df:any;
  chartshow=true;
  setPalika(id:any){
    this.chartshow=false;
    // this.imgShow=true;
    this.df=id;
    this.activeStates[id] = !this.activeStates[id];
    let ind=this.srchForm.value.mindicator;
   if(ind!=null){
    this.isLoading = true;
    let postData = {
      "program": "1",
      "charttype": "column",
      "myear": this.fy,
      "mindicator": ind
    };
    this.DS.createOpen(postData,"h",this.df).subscribe({
      next:(result:any) => {
        // console.log(result);
        this.chartshow=true;
        this.imgShow=false;
        
          this.highchart = new Chart({
            chart: {
              type: 'column'
          },
          title: result.data[0].title,
          subtitle: {
              text: 'Source: HMIS'
          },
          xAxis: {
              categories: result.data[0].cats,
              crosshair: true
          },
          yAxis: {
              min: 0,
              title: {
                  text: result.data[0].ntype
              }
          },
          tooltip: {
              headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
              pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                  '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
              footerFormat: '</table>',
              shared: true,
              useHTML: true
          },
          plotOptions: {
            
              column: {
                  pointPadding: 0.2,
                  borderWidth: 0,
                  dataLabels: {
                    enabled: true
                  },
              }
          },
          series: result.data[0].series
        
          });
          this.isLoading = false; 
   
    }, error:err => {
      this.toastr.error("Unable to fetch data from HMIS", 'Error');
      this.isLoading = false; 
    }
    });
   }
  }

  currentIndex = 0;
  currentImage: any;
  interval: any;
  isMouseOver = false; 

  

  showSlide() {
    this.currentImage = this.slider[this.currentIndex];
  }

  startSlide() {
    this.interval = setInterval(() => {
      this.currentIndex = (this.currentIndex + 1) % this.slider.length;
      this.showSlide();
    }, 5000); // Change slide every 3 seconds (3000 milliseconds)
  }

  onMouseOver() {
    this.isMouseOver = true;
    clearInterval(this.interval); // Stop the slideshow when mouse is over
  }
  
  onMouseOut() {
    this.isMouseOver = false;
    this.startSlide(); // Resume slideshow when mouse leaves
  }

  showPrevious() {
    this.currentIndex = (this.currentIndex - 1 + this.slider.length) % this.slider.length;
    this.showSlide();
  }

  showNext() {
    this.currentIndex = (this.currentIndex + 1) % this.slider.length;
    this.showSlide();
  }

  ngOnDestroy() {
    clearInterval(this.interval); // Clear the interval on component destruction to avoid memory leaks
  }

  isActive(itemId: number): boolean {
    // Check if the item is active or not
    return this.activeStates[itemId] || false;
  }


  getList(pageno?: number | undefined) {
    const page = pageno || 1;
    this.RS.getListOpen(this.pagination.perPage, page, this.searchTerm, this.column, this.isDesc).subscribe({
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

  initCharts(): void {
    this.mainChart = this.chartsData.mainChart;
  }

  setTrafficPeriod(value: string): void {
    this.trafficRadioGroup.setValue({ trafficRadio: value });
    this.chartsData.initMainChart(value);
    this.initCharts();
  }
}
