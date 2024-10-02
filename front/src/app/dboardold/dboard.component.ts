import { AfterViewInit, Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { DboardService } from './dboard.service';
import { ChartjsModule } from '@coreui/angular-chartjs';
import { Chart } from 'angular-highcharts';
import * as Highcharts from 'highcharts';
import { LoginService } from '../login/login.service';
import * as $ from 'jquery';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
@Component({
  selector: 'app-dboard',
  templateUrl: './dboard.component.html',
  styleUrls: ['./dboard.component.scss']
})
export class DboardssComponent implements OnInit,AfterViewInit {

  

  cForm: FormGroup
  cmodel: any = {};
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
  program:any;
  indicators:any;
  ddata:any;
  sindicator:any;
  ctype="bar";
  letters = '0123456789ABCDEF';
  composites:any;
  range = new Array();
  caption = new Array();
  cdata = new Array();
  myArray=new Array();
  f=new Array();
  dashdata:any;
  dashdata2:any;
  dashdata3:any;
  dashdata4:any;
  dashdata5:any;
  default:any;
  das1=0;
  das2=0;
  das3=0;
  das4=0;
  das5=0;
  b1=0;
  b2=0;
  b3=0;
  mind:any;
  mfy:any;
  usertype:any;
  tablehtml:any;
  modalRef?: BsModalRef;
  

  // add point to chart serie
  // add() {
  //   this.chart.addPoint(Math.floor(Math.random() * 10));
  // }

  // months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  // months=["Shrawn","Bhadra","Ashoj","Kartik","Mangsir","Poush","Magh","Falgun","Chaitra","Baishakh","Jestha","Ashar"];
  months=["श्रावण ","भदौ","असोज ","कार्तिक","मंसिर ","पौष","माघ","फाल्गुण","चैत्र","वैशाख","ज्येष्ठ ","असार"];
 
  nutrition:any;
  opd:any;
  fp:any;
  safem:any;
  delivery:any;
  sector:any;
  sectorchart:any;
  sectoract:any;
  sectoractchart:any;
  broadsector:any;
  broadsectorchart:any;
  highchart:any;
  configFormLayout: any;
  configForm: any;
  dcharts:any;
  
  
  chartBarData = {
    labels: [...this.months].slice(0, 12),
    datasets: [
      {
        label: 'Hmis Indicator',
        backgroundColor: '#f87979',
        height: '50px',
        data: []
      }
    ]
  };

  chartPieData = {
    labels:[...this.months].slice(0, 12),
    datasets: [
      {
        data: [],
        backgroundColor: ['#fde23e','#f16e23', '#57d9ff','#937e88','#3de23e','#416e23', '#c9de53','#c5b2d8','#ec8888','#5957da','#f90303','#d6aa64'],
        hoverBackgroundColor: ['#fde23e','#f16e23', '#57d9ff','#937e88','#3de23e','#416e23', '#c9de53','#c5b2d8','#ec8888','#5957da','#f90303','#d6aa64'],
      }
    ]
  };

  chartLineData = {
    labels: [...this.months].slice(0, 12),
    datasets: [
      {
        label: 'HMIS Indicator',
        backgroundColor: 'rgba(220, 220, 220, 0.2)',
        borderColor: 'rgba(220, 220, 220, 1)',
        pointBackgroundColor: 'rgba(220, 220, 220, 1)',
        pointBorderColor: '#fff',
        data:[]
      }
     
    ]
  };

  chartDoughnutData = {
    labels:  [...this.months].slice(0, 12),
    datasets: [
      {
        backgroundColor: ['#fde23e','#f16e23', '#57d9ff','#937e88','#3de23e','#416e23', '#c9de53','#c5b2d8','#ec8888','#5957da','#f90303','#d6aa64'],
        data: []
      }
    ]
  };

  chartLineData2 = {
    labels: ['2076/77','2077/78','2078/79'],
    datasets: [
      {
        label: 'Average MSS score (Availability and readiness)',
               backgroundColor: 'rgba(220, 220, 220, 0.2)',
                borderColor: '#198754',
                pointBackgroundColor: '#e55353',
                pointBorderColor: '#6610f2',
        data: [10,20,12]
      },
      {
        label: 'Aggregate LEAF score (Leadership and performance)',
        backgroundColor: 'rgba(220, 220, 220, 0.2)',
        borderColor: '#6f42c1',
        pointBackgroundColor: '#f9b115',
        pointBorderColor: '#f9b115',
        data: [9,16,17]
      }
    ]
  };
  fformLayout: { id: never[]; findicator: string[]; fyear: string[]; fcharttype: string[]; };
  fForm: FormGroup;
  findicators=[{id:1,name:"Budget and Expenditure"},
  {id:2,name:"Sectorial Budget and Expenditure"},
  {id:3,name:"Sectorial Activity Budget and Expenditure"},
  {id:4,name:"Broad Sector Budget and Expenditure"}]
  financechart: any;
  cid:any;
  fcharts: any;

  // chartLineOptions = {
  //   maintainAspectRatio: false,
  // };


  constructor(private RS: DboardService, private toastr: ToastrService, private fb: FormBuilder,private loginService: LoginService,private modalService: BsModalService) { 
    
    this.formLayout = {
      id:[],
      program: ['1',Validators.required],
      indicator: [''],
      dyear: [''],
      charttype: ['bar'],
      myear:[''],
      mindicator:['']
    }

    this.fformLayout = {
      id:[],
      findicator: [''],
      fyear: [''],
      fcharttype: ['column'],
    }
    
    this.cForm =fb.group(this.formLayout)

    this.srchForm = new FormGroup({
      entries: new FormControl('10'),
      srch_term: new FormControl(''),
      
    });

    this.configFormLayout = {
      cid: ['0',],
      name: ['', [Validators.required]],
      order: ['', [Validators.required]],
      time: ['15', [Validators.required]]
    }

    this.configForm = fb.group(this.configFormLayout)

    this.fForm = fb.group(this.fformLayout)

  }
  ngAfterViewInit(): void {
  if(this.usertype=="hmis"){
    $("#log").show();
  }else{
    // alert("hi");
    $("#log").hide();
  }
  }

  ngOnInit(): void {
    this.usertype=localStorage.getItem("usertype");
    if(this.usertype=="hmis"){
      $("#log").show();
    }else{
      // alert("hi");
      $("#log").hide();
    }
    // alert(this.usertype);
    // this.getUserinfo();
    this.pagination.perPage = this.perPages[0];
    this.getList();
    this.getDefault();
    this.getProgram();
    this.getSectorData();
    this.getSectorActivityData();
    this.getBroadSectorData();
    this.sindicator="";
    // this.getComposite();
    this.getIndicators(1);
    // this.getData('vH9Mm6o3LKn',2076);
    this.getDashboard1(5);
    this.getDashboard2(16);
    this.getDashboard3(3);
    this.getDashboard4(1);
    this.getDashboard5(1);
    
    this.getChartConfig();
    this.getFinanceChartConfig();
      
    
    
   
  }

  fFormSubmit(){
    
      if (this.fForm.valid) {
        this.model = this.fForm.value;
        // console.log(this.model);
        this.createFItem(this.fForm.value.id);
       
      } else {
        Object.keys(this.cForm.controls).forEach(field => {
          const singleFormControl = this.cForm.get(field);
          singleFormControl?.markAsTouched({onlySelf: true});
        });
      }
    
  }
createFItem(id:any){
  let upd=this.model;
    this.RS.create(upd,"f").subscribe({
      next:(result:any) => {
        $("#savebtnf").show();
          this.financechart = new Chart({
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
                  text: 'No.'
              }
          },
          tooltip: {
              headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
              pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                  '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
        
   
    }, error:err => {
      this.toastr.error(err.error.message, 'Error');
    }
    });
}


  getChartConfig(){
    this.RS.getConfig(0).subscribe({
      next: (result:any) => {
           this.dcharts = result.data; 
           this.createCharts(result.data);
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getFinanceChartConfig(){
    this.RS.getConfig(1).subscribe({
      next: (result:any) => {
           this.fcharts = result.data; 
           this.createFinanceCharts(result.data);
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  createFinanceCharts(data:any){
    for (let i = 0; i < data.length; i++) {
    
      let chartname="cc"+data[i].id;
      this.highcharts[chartname]="";
    }
    for (let i = 0; i < data.length; i++) {
      let chartname="cc"+data[i].id;
      this.RS.createfinanceCharts(data[i].myear,data[i].mindicator,data[i].charttype,"f").subscribe({
        next:(result:any) => {
          if(this.cForm.value.charttype=="table"){
           
          }else{
            
            this.highcharts[chartname] = new Chart({
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
                    text: 'No.'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
          }
       
      }, error:err => {
        this.toastr.error(err.error.message, 'Error');
      }
      });
      
    }
  }

  highcharts:any = {};
  createCharts(data:any){
    for (let i = 0; i < data.length; i++) {
    
      let chartname="cc"+data[i].id;
      this.highcharts[chartname]="";
    }
    for (let i = 0; i < data.length; i++) {
      let chartname="cc"+data[i].id;
      this.RS.createCharts(data[i].myear,data[i].mindicator,data[i].charttype,"h").subscribe({
        next:(result:any) => {
          if(this.cForm.value.charttype=="table"){
           
          }else{
            
            this.highcharts[chartname] = new Chart({
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
                    text: 'No.'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
          }
       
      }, error:err => {
        this.toastr.error(err.error.message, 'Error');
      }
      });
      
    }
  }

  getInd(ind:any){
    console.log(ind);
    this.mind=ind;
  }

  getIndData(fy:any){
    this.mfy=fy;
  }

  getUserinfo(){

    this.loginService.getuserinfo().subscribe({
      next: (result:any) => {
          console.log(result.data[0]);
          this.usertype=result.data[0].usertype;
          // if(this.usertype=="hmis"){
          //   // alert("hello");
          //   $("#logtable").show();
          // }else{
          //   $("#logtable").hide();
          // }
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
    // console.log(this.loginService.getuserinfo());
    // this.orgs=this.loginService.getuserinfo();
  }


  getBroadSectorData(){
    this.RS.getSectorData("broad-sector").subscribe({
      next: (result:any) => {
           this.broadsector = result;
          //  console.log(result);
           var tbudget = 0;
		      	var texp = 0;
            var labels = [];
           var seriess = [{
            type:'column',
            name: "Budget",
            data: [Array]
        }, {
           type:'column',
            name: "Expenditure",
            data: [Array]
        }];
        // console.log(series);
        for (var i in result) {
          labels.push(result[i].namenp);
          seriess[0].data.push(result[i].totalbudget);
          seriess[1].data.push(result[i].expenditure);
          tbudget += result[i].totalbudget;
          texp += result[i].expenditure;
      }
     
      this.broadsectorchart = new Chart({
        chart: {
          type: 'column'
      },
      title: {
          text: ' Broad Sector Budget and Expenditure'
      },
      subtitle: {
          text: 'Source: Sutra'
      },
      xAxis: {
          categories: labels,
          crosshair: true
      },
      yAxis: {
          min: 0,
          title: {
              text: 'Nrs.'
          }
      },
      tooltip: {
          headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
          pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
              '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
      series: [{
          type:'column',
          name: seriess[0].name,
          data: seriess[0].data.slice(1).map(Number)
  
      }, {
          type:'column',
          name: seriess[1].name,
          data: seriess[1].data.slice(1).map(Number)
  
      }]
    
      });
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getSectorActivityData(){
    this.RS.getSectorData("sectorial-activity").subscribe({
      next: (result:any) => {
           this.sectoract = result;
          //  console.log(result);
           var tbudget = 0;
		      	var texp = 0;
            var labels = [];
           var seriess = [{
            type:'column',
            name: "Budget",
            data: [Array]
        }, {
           type:'column',
            name: "Expenditure",
            data: [Array]
        }];
        // console.log(series);
        for (var i in result) {
          labels.push(result[i].namenp);
          seriess[0].data.push(result[i].totalbudget);
          seriess[1].data.push(result[i].expenditure);
          tbudget += result[i].totalbudget;
          texp += result[i].expenditure;
      }
     
      this.sectoractchart = new Chart({
        chart: {
          type: 'column'
      },
      title: {
          text: ' Sectorial Activity Budget and Expenditure'
      },
      subtitle: {
          text: 'Source: Sutra'
      },
      xAxis: {
          categories: labels,
          crosshair: true
      },
      yAxis: {
          min: 0,
          title: {
              text: 'Nrs.'
          }
      },
      tooltip: {
          headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
          pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
              '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
      series: [{
          type:'column',
          name: seriess[0].name,
          data: seriess[0].data.slice(1).map(Number)
  
      }, {
          type:'column',
          name: seriess[1].name,
          data: seriess[1].data.slice(1).map(Number)
  
      }]
    
      });
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getSectorData(){
    this.RS.getSectorData("sector").subscribe({
      next: (result:any) => {
           this.sector = result;
          //  console.log(result);
           var tbudget = 0;
		      	var texp = 0;
            var labels = [];
           var seriess = [{
            type:'column',
            name: "Budget",
            data: [Array]
        }, {
           type:'column',
            name: "Expenditure",
            data: [Array]
        }];
        // console.log(series);
        for (var i in result) {
          labels.push(result[i].namenp);
          seriess[0].data.push(result[i].totalbudget);
          seriess[1].data.push(result[i].expenditure);
          tbudget += result[i].totalbudget;
          texp += result[i].expenditure;
      }
      // console.log(labels);
      // console.log(seriess[0].data.slice(1));
      // console.log(result);
      this.sectorchart = new Chart({
        chart: {
          type: 'column'
      },
      title: {
          text: ' Sectorial Budget and Expenditure'
      },
      subtitle: {
          text: 'Source: Sutra'
      },
      xAxis: {
          categories: labels,
          crosshair: true
      },
      yAxis: {
          min: 0,
          title: {
              text: 'Nrs.'
          }
      },
      tooltip: {
          headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
          pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
              '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
      legend: {
        align: 'right',
        x: -30,
        verticalAlign: 'top',
        y: 25,
        floating: true,
        backgroundColor: 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false
    },
      series: [{
          type:'column',
          name: seriess[0].name,
          data: seriess[0].data.slice(1).map(Number)
  
      }, {
          type:'column',
          name: seriess[1].name,
          data: seriess[1].data.slice(1).map(Number)
  
      }]
    
      });
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getDefault(){
    this.RS.getDefault().subscribe({
      next: (result:any) => {
           this.default = result.data; 
           var def=this.default;
          //  console.log(this.default);
           for (let i = 0; i < def.length; i++) {
            if(def[i].pid==5){
              this.das1=1;
            }
            if(def[i].pid==16){
              this.das2=1;
            }
            if(def[i].pid==3){
              this.das3=1;
            }
            if(def[i].pid==1 && def[i].tid==0){
              this.das4=1;
            }
            if(def[i].pid==1 && def[i].tid==1){
              this.das5=1;
            }
            if(def[i].pid==100){
              this.b1=1;
            }
            if(def[i].pid==101){
              this.b2=1;
            }
            if(def[i].pid==102){
              this.b3=1;
            }
          }
          // console.log(this.das2);
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getProgram(){
    this.RS.getProgram().subscribe({
      next: (result:any) => {
           this.program = result.data; 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }
  getToggle(pid:any,fp:any,td:any,event:any){
  
    this.RS.editSetting(pid,fp,td).subscribe({
      next: (result:any) => {
        this.toastr.success("Setting saved Successfully!", 'Success'); 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  changeStatus(id:any,event:any){
    this.RS.SaveSetting(id).subscribe({
      next: (result:any) => {
        this.toastr.success("Setting saved Successfully!", 'Success'); 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }
 
  getComposite(){
    let i=0;
    let c1=['#198754','#6f42c1','red','blue','#f9b115','#6610f2'];
    let c2=['#f9b115','#6610f2','blue','red','#198754','#6f42c1'];
    
    this.RS.getComposite().subscribe({
      next: (result:any) => {
           this.composites = result.data; 
           for(i=0;i<this.composites.length;i++){
            this.range.push(this.composites[i].fys);
            this.caption.push(this.composites[i].indicator);
            this.cdata.push(this.composites[i].value);
           }
           
           var mySet = new Set(this.range);
            this.range = [...mySet];
         
           var cap=new Set(this.caption);
           this.caption=[...cap];
           for(i=0;i<this.caption.length;i++){
            this.myArray.push({label: this.caption[i], backgroundColor: 'rgba(220, 220, 220, 0.2)',
                      borderColor: c1[i],
                      pointBackgroundColor: c2[i],
                      pointBorderColor: c1[i],
                      data:[this.cdata[i],this.cdata[i+2]]});
           }
          //  console.log(this.myArray);
           this.chartLineData2 = {
            labels: this.range,
            datasets: this.myArray,
            // [
              
              // {
              //   label: 'Average MSS score (Availability and readiness)',
              //   backgroundColor: 'rgba(220, 220, 220, 0.2)',
              //           borderColor: '#198754',
              //           pointBackgroundColor: '#e55353',
              //           pointBorderColor: '#6610f2',
              //   data: [10,20,12]
              // },
              // {
              //   label: 'Aggregate LEAF score (Leadership and performance)',
              //   backgroundColor: 'rgba(220, 220, 220, 0.2)',
              //   borderColor: '#6f42c1',
              //   pointBackgroundColor: '#f9b115',
              //   pointBorderColor: '#f9b115',
              //   data: [9,16,17]
              // }
            // ]
          };
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getIndicators(pid:any){
  
    this.RS.getIndicators(pid).subscribe({
      next: (result:any) => {
           this.indicators = result.data; 
       },
       error : err => {
         this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getDashboard1(pid:any){
    this.RS.getDashboardCombineData(pid,0).subscribe({
      next: (result:any) => {
           this.dashdata = result.data; 
          //  console.log(this.dashdata);
           var d1=this.dashdata[0].data.map(Number);
           var d2=this.dashdata[1].data.map(Number);
           var d3=this.dashdata[2].data.map(Number);
           var d4=this.dashdata[3].data.map(Number);
           var d5=this.dashdata[4].data.map(Number);
           var d6=this.dashdata[5].data.map(Number);
           var d7=this.dashdata[6].data.map(Number);
           const reducer = (accumulator: any, curr: any) => accumulator + curr;
           d1=d1.reduce(reducer);
           d2=d2.reduce(reducer);
           d3=d3.reduce(reducer);
           d4=d4.reduce(reducer);
           d5=d5.reduce(reducer);
           d6=d6.reduce(reducer);
           d7=d7.reduce(reducer);
         
          //  console.log(d1.data);
          this.fp = new Chart({
            chart: {
              plotShadow: false,
              type: 'pie'
          },
          title: {
              text: 'परिवार नियोजन 2078/79'
          },
          tooltip: {
              pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
          },
          accessibility: {
              point: {
                  valueSuffix: '%'
              }
          },
          plotOptions: {
              pie: {
                  allowPointSelect: true,
                  cursor: 'pointer',
                  dataLabels: {
                      enabled: true,
                      format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                  }
              }
          },
          series: [{
              type:'pie',
              name: 'FP Methods',
              colorByPoint: true,
              data: [{
                  name: this.dashdata[0].inamenp,
                  y: d1,
              }, {
                  name: this.dashdata[1].inamenp,
                  y: d2
              }, {
                  name: this.dashdata[2].inamenp,
                  y: d3
              }, {
                  name: this.dashdata[3].inamenp,
                  y: d4
              }, {
                  name: this.dashdata[4].inamenp,
                  y: d5
              }, {
                  name: this.dashdata[5].inamenp,
                  y: d6
              }, {
                  name: this.dashdata[6].inamenp,
                  y: d7
              }]
          }]
          });
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getDashboard2(pid:any){
    this.RS.getDashboardCombineData(pid,0).subscribe({
      next: (result:any) => {
           this.dashdata2 = result.data; 
          //  console.log(this.dashdata);
          var d1=this.dashdata2[0].data.map(Number);
          var d2=this.dashdata2[1].data.map(Number);
          // console.log(d1);
           this.opd = new Chart({
            chart: {
              type: 'column'
          },
          title: {
              text: 'कुल नयाँ ओपीडी भ्रमण'+" "+ this.dashdata2[0].fys
          },
          xAxis: {
              categories: this.months
          },
          yAxis: {
              min: 0,
              title: {
                  text: 'Total Number'
              },
              stackLabels: {
                  enabled: true,
                  style: {
                      fontWeight: 'bold',
                      color: 'gray',
                      textOutline: 'none'
                  }
              }
          },
          legend: {
              align: 'right',
              x: -30,
              verticalAlign: 'top',
              y: 25,
              floating: true,
              backgroundColor: 'white',
              borderColor: '#CCC',
              borderWidth: 1,
              shadow: false
          },
          tooltip: {
              headerFormat: '<b>{point.x}</b><br/>',
              pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
          },
          plotOptions: {
              column: {
                  stacking: 'normal',
                  dataLabels: {
                      enabled: true
                  }
              }
          },
          series: [{
              type: 'column',
              name: this.dashdata2[0].inamenp,
              data: d1
          }, {
             type: 'column',
              name: this.dashdata2[1].inamenp,
              data: d2
          }]
        
          });
        
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

 
  getDashboard3(pid:any){
    this.RS.getDashboardCombineData(pid,0).subscribe({
      next: (result:any) => {
           this.dashdata3 = result.data; 
          //  console.log(this.dashdata);
          var d1=this.dashdata3[0].data.map(Number);
          var d2=this.dashdata3[1].data.map(Number);
          var d3=this.dashdata3[2].data.map(Number);
          // console.log(d1);
           this.nutrition = new Chart({
            chart: {
              type: 'column'
          },
          title: {
              text: 'पोषण'+" "+ this.dashdata3[0].fys
          },
          xAxis: {
              categories: this.months
          },
          yAxis: {
              min: 0,
              title: {
                  text: 'Total Number'
              },
              stackLabels: {
                  enabled: true,
                  style: {
                      fontWeight: 'bold',
                      color: 'gray',
                      textOutline: 'none'
                  }
              }
          },
          legend: {
              align: 'right',
              x: -30,
              verticalAlign: 'top',
              y: 25,
              floating: true,
              backgroundColor: 'white',
              borderColor: '#CCC',
              borderWidth: 1,
              shadow: false
          },
          tooltip: {
              headerFormat: '<b>{point.x}</b><br/>',
              pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
          },
          plotOptions: {
              column: {
                  stacking: 'normal',
                  dataLabels: {
                      enabled: true
                  }
              }
          },
          series: [{
              type: 'column',
              name: this.dashdata3[0].inamenp,
              data: d1
          }, {
             type: 'column',
              name: this.dashdata3[1].inamenp,
              data: d2
          },{
            type: 'column',
             name: this.dashdata3[2].inamenp,
             data: d3
         }]
        
          });
        
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getDashboard4(pid:any){
    this.RS.getDashboardCombineData(pid,0).subscribe({
      next: (result:any) => {
           this.dashdata4 = result.data; 
          //  console.log(this.dashdata);
          var d1=this.dashdata4[0].data.map(Number);
          var d2=this.dashdata4[1].data.map(Number);
          var d3=this.dashdata4[2].data.map(Number);
          // console.log(d1);
           this.safem = new Chart({
            chart: {
              type: 'column'
          },
          title: {
              text: 'सुरक्षित मातृत्व कार्यक्रम'+" "+ this.dashdata4[0].fys
          },
          xAxis: {
              categories: this.months
          },
          yAxis: {
              min: 0,
              title: {
                  text: 'Total Number'
              },
              stackLabels: {
                  enabled: true,
                  style: {
                      fontWeight: 'bold',
                      color: 'gray',
                      textOutline: 'none'
                  }
              }
          },
          legend: {
              align: 'right',
              x: -30,
              verticalAlign: 'top',
              y: 25,
              floating: true,
              backgroundColor: 'white',
              borderColor: '#CCC',
              borderWidth: 1,
              shadow: false
          },
          tooltip: {
              headerFormat: '<b>{point.x}</b><br/>',
              pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
          },
          plotOptions: {
              column: {
                  stacking: 'normal',
                  dataLabels: {
                      enabled: true
                  }
              }
          },
          series: [{
              type: 'column',
              name: this.dashdata4[0].inamenp,
              data: d1
          }, {
             type: 'column',
              name: this.dashdata4[1].inamenp,
              data: d2
          },{
            type: 'column',
             name: this.dashdata4[2].inamenp,
             data: d3
         }]
        
          });
         
        
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  getDashboard5(pid:any){
    this.RS.getDashboardCombineData(pid,1).subscribe({
      next: (result:any) => {
           this.dashdata5 = result.data; 
          //  console.log(this.dashdata);
          var d1=this.dashdata5[0].data.map(Number);
          var d2=this.dashdata5[1].data.map(Number);
           this.delivery = new Chart({
            chart: {
              type: 'column'
          },
          title: {
              text: 'सुरक्षित मातृत्व कार्यक्रम - प्रसुति सेवा'+" "+ this.dashdata5[0].fys
          },
          xAxis: {
              categories: this.months
          },
          yAxis: {
              min: 0,
              title: {
                  text: 'Total Number'
              },
              stackLabels: {
                  enabled: true,
                  style: {
                      fontWeight: 'bold',
                      color: 'gray',
                      textOutline: 'none'
                  }
              }
          },
          legend: {
              align: 'right',
              x: -30,
              verticalAlign: 'top',
              y: 25,
              floating: true,
              backgroundColor: 'white',
              borderColor: '#CCC',
              borderWidth: 1,
              shadow: false
          },
          tooltip: {
              headerFormat: '<b>{point.x}</b><br/>',
              pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
          },
          plotOptions: {
              column: {
                  stacking: 'normal',
                  dataLabels: {
                      enabled: true
                  }
              }
          },
          series: [{
              type: 'column',
              name: this.dashdata5[0].inamenp,
              data: d1
          }, {
             type: 'column',
              name: this.dashdata5[1].inamenp,
              data: d2
          }]
        
          });
        
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }


  
  getData(iid:any,fy:any){
    this.ddata=[];
    this.RS.getDashboardData(iid,fy).subscribe({
      next: (result:any) => {
           this.ddata = result.data; 
          //  console.log(this.ddata);
           if(this.ctype=="bar"){
           this.chartBarData = {
            labels: [...this.months].slice(0, 12),
            datasets: [
              {
                label: this.sindicator,
                backgroundColor: '#f87979',
                height:'200',
                data: this.ddata
              }
            ]
          };
        }
        if(this.ctype=="pie"){
          this.chartPieData = {
            labels:[...this.months].slice(0, 12),
            datasets: [
              {
                data: this.ddata,
                backgroundColor:['#fde23e','#f16e23', '#57d9ff','#937e88','#3de23e','#416e23', '#c9de53','#c5b2d8','#ec8888','#5957da','#f90303','#d6aa64'],
                hoverBackgroundColor: ['#fde23e','#f16e23', '#57d9ff','#937e88','#3de23e','#416e23', '#c9de53','#c5b2d8','#ec8888','#5957da','#f90303','#d6aa64']
              }
            ]
          };
        }

        if(this.ctype=="line"){
          this.chartLineData = {
            labels: [...this.months].slice(0, 12),
            datasets: [
              {
                label: this.sindicator,
                backgroundColor: 'rgba(220, 220, 220, 0.2)',
                borderColor: '#198754',
                pointBackgroundColor: '#e55353',
                pointBorderColor: '#6610f2',
                data:this.ddata
              }
             
            ]
          };
        }

        if(this.ctype=='doughnut'){
          this.chartDoughnutData = {
            labels:  [...this.months].slice(0, 12),
            datasets: [
              {
                backgroundColor: ['#fde23e','#f16e23', '#57d9ff','#937e88','#3de23e','#416e23', '#c9de53','#c5b2d8','#ec8888','#5957da','#f90303','#d6aa64'],
                data: this.ddata
              }
            ]
          };
        }
       },
       error : err => {
        //  this.toastr.error(err.error, 'Error');
       }
       
     }
     );
  }

  updateInd(ind:String,iid:any,fy:any){
    this.sindicator=ind;
    this.getData(iid,fy);

  }

  // updateCtype(){
  //   //this.ctype=ct;
  //   //this.getData(iid,fy);
  // }

  updateCtype(ct:any,iid:any,fy:any){
    this.ctype=ct;
    this.getData(iid,fy);
  }

  saveConfig(template: TemplateRef<any>,cid:any){
    this.configForm.patchValue({
      'cid': cid,  
    });
    this.modalRef = this.modalService.show(template,
      Object.assign({}, { class: 'gray modal-lg' }));
  }

  saveFinanceConfig(template: TemplateRef<any>,cid:any){
  
    this.configForm.patchValue({
      'cid': cid,  
    });
    // this.cid=cid;
    this.modalRef = this.modalService.show(template,
      Object.assign({}, { class: 'gray modal-lg' }));
  }

  configFormSubmit(){
    if (this.configForm.valid) {
      this.cmodel = this.configForm.value;
      // console.log(this.model);
      this.createConfig(this.configForm.value.cid);
    } else {
      Object.keys(this.configForm.controls).forEach(field => {
        const singleFormControl = this.configForm.get(field);
        singleFormControl?.markAsTouched({onlySelf: true});
      });
    }
  }

  createConfig(cid:any) {

    let upd = this.cmodel;
    if(cid==0){
      this.RS.saveConfig(upd,this.cForm.value.myear,this.cForm.value.mindicator,this.cForm.value.charttype,cid).subscribe({
        next: (result :any) => {
        this.toastr.success('Item Successfully Saved!', 'Success');
        this.configForm =this.fb.group(this.configFormLayout);
        this.cForm =this.fb.group(this.formLayout);
         this.modalRef?.hide()
         $("#savebtn").hide();
         this.getChartConfig();
      }, error :err=> {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    }else{
      this.RS.saveConfig(upd,this.fForm.value.fyear,this.fForm.value.findicator,this.fForm.value.fcharttype,cid).subscribe({
        next: (result :any) => {
        this.toastr.success('Item Successfully Saved!', 'Success');
        this.configForm =this.fb.group(this.configFormLayout);
        this.fForm =this.fb.group(this.formLayout);
         this.modalRef?.hide()
         $("#savebtnf").hide();
         this.getChartConfig();
      }, error :err=> {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    }

      
    
  }

  cFormSubmit(){
    if (this.cForm.valid) {
      this.model = this.cForm.value;
      console.log(this.model);
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
        // this.cForm = this.fb.group(this.formLayout)
        // this.getList();
      }, error :err=> {
        this.toastr.error(err.error.message, 'Error');
      }
      });
    } else {
      this.RS.create(upd,"h").subscribe({
        next:(result:any) => {
          if(this.cForm.value.charttype=="table"){
            $("#table").show();
            $("#chartdiv").hide();
            $("#table").html(result.data);
            $("#savebtn").show();
            // this.tablehtml=result.data.replace(/^"(.+(?="$))"$/, '$1');
          }else{
            $("#table").hide();
            $("#chartdiv").show();
            $("#savebtn").show();
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
                    text: 'No.'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
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
          }
     
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
        this.getChartConfig();
        // this.getList();
      }, error:(error: { error: any; }) => {
        this.toastr.error(error.error, 'Error');
      }});
    }
  
  }

  popdistmf = new Chart({
    chart: {
        type: 'bar'
    },
    title: {
        text: 'Population pyramid for <<ORG>>, 2079'
    },
    subtitle: {
        text: 'Source: <a href="https://hmis.gov.np">HMIS</a>'
    },
    accessibility: {
        point: {
            valueDescriptionFormat: '{index}. Age {xDescription}, {value}%.'
        }
    },
    xAxis: [{
        categories: [
          '0-4', '5-9', '10-14', '15-19',
          '20-24', '25-29', '30-34', '35-39', '40-44',
          '45-49', '50-54', '55-59', '60-64', '65-69',
          '70-74', '75-79', '80-84', '85-89', '90-94',
          '95-99', '100 + '
      ],
        reversed: false,
        labels: {
            step: 1
        },
        accessibility: {
            description: 'Age (male)'
        }
    }, { // mirror axis on right side
        opposite: true,
        reversed: false,
        categories: [
          '0-4', '5-9', '10-14', '15-19',
          '20-24', '25-29', '30-34', '35-39', '40-44',
          '45-49', '50-54', '55-59', '60-64', '65-69',
          '70-74', '75-79', '80-84', '85-89', '90-94',
          '95-99', '100 + '
      ],
        linkedTo: 0,
        labels: {
            step: 1
        },
        accessibility: {
            description: 'Age (female)'
        }
    }],
    yAxis: {
        title: {
            text: null
        },
        labels: {
            formatter: (fob:any) => {
                return Math.abs(fob.value) + '%';
            }
        },
        accessibility: {
            description: 'Percentage population',
            rangeDescription: 'Range: 0 to 5%'
        }
    },

    plotOptions: {
        series: {
            stacking: 'normal'
        }
    },

    tooltip: {
        formatter: (fob:any) =>{
          // console.log(fob);
            return '<b>' + fob.series.name + ', age ' + fob.point.category + '</b><br/>' +
                'Population: ' + Math.ceil(Math.abs(fob.point.y)) + '%';
        }
    },

    series: [{
      type:'bar',
      name: 'Male',
      color:'red',
        data: [
            -2.2, -2.1, -2.2, -2.4,
            -2.7, -3.0, -3.3, -3.2,
            -2.9, -3.5, -4.4, -4.1,
            -3.4, -2.7, -2.3, -2.2,
            -1.6, -0.6, -0.3, -0.0,
            -0.0
        ]
    }, {
        name: 'Female',
        type:'bar',
        color:'green',
        data: [
            2.1, 2.0, 2.1, 2.3, 2.6,
            2.9, 3.2, 3.1, 2.9, 3.4,
            4.3, 4.0, 3.5, 2.9, 2.5,
            2.7, 2.2, 1.1, 0.6, 0.2,
            0.0
        ]
    }]
});

fyRng = this.fyRange();

fyRange(){
  const fys = [];
  for(let i=2079; i>=2070;i--){
    fys.push({'v':i,'label':(i+'/'+((i+1)+"").slice(-2))});
  }
  return fys;
}

}
