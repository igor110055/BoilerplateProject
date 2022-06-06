import moment from 'moment';
// 안써도 자동으로 한국 시간을 불러온다. 명확하게 하기 위해 import
import 'moment/locale/ko';
export class datetimeRenderer {
  constructor(props) {
      const el = document.createElement('div');
      el.className="tui-grid-cell-content"
      const { format ,source} = props.columnInfo.renderer.options;
      this.format = format;  /*YYYYMMDDHHmmss    이게 풀양식이다.*/
      this.source = source;  /*TIME 초, YYYYMMDD , YYYYMMDDHHmm,  YYYYMMDDHHmmss  */

      this.el = el;
      this.render(props);
      
    }

    getElement() {
      return this.el;
    }

    render(props) {
    /*  날짜관련해서  mement 라이브러리를 설치했다.
var myDate = new Date("2015-06-17 14:24:36");
console.log(moment(myDate).format("YYYY-MM-DD HH:mm:ss"));
console.log("Date: "+moment(myDate).format("YYYY-MM-DD"));
console.log("Year: "+moment(myDate).format("YYYY"));
console.log("Month: "+moment(myDate).format("MM"));
console.log("Month: "+moment(myDate).format("MMMM"));
console.log("Day: "+moment(myDate).format("DD"));
console.log("Day: "+moment(myDate).format("dddd"));
console.log("Time: "+moment(myDate).format("HH:mm")); // Time in24 hour format
console.log("Time: "+moment(myDate).format("hh:mm A"));
   	    */
   	        if(this.source=='YYYYMMDDHHmmss'){
	   	      	this.el.innerText = moment(props.value,'YYYYMMDDHHmmss').format(this.format);
   	        } else if(this.source=='YYYYMMDDHHmm'){
				this.el.innerText = moment(props.value,'YYYYMMDDHHmm').format(this.format);
   	        } else if(this.source=='YYYYMMDD'){
   	        	this.el.innerText = moment(props.value,'YYYYMMDD').format(this.format);
   	        } else if(this.source=='TIME'){
                if(!isNaN(props.value)){
                    var tmp = new Date(Number(props.value));
                    this.el.innerText = moment(tmp).format(this.format);
                } else {
                    this.el.innerText = props.value;    
                }
   	        	
   	        } else {
   	        	this.el.innerText = props.value;
   	        }
   	        
     
    }
  }