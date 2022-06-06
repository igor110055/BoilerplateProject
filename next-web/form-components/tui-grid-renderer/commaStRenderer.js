import * as pjtUtil from '@/utils/pjtUtil'
export class commaStRenderer {
  constructor(props) {
      const el = document.createElement('div');
      el.className="tui-grid-cell-content"
      const { src ,tgt } = props.columnInfo.renderer.options;
      this.el = el;
      this.src = src;
      this.tgt = tgt;
      this.render(props);
      
    }

    getElement() {
      return this.el;
    }

    render(props) {
        //console.log(this.el);
        //console.log(this.el);
      
        if(this.el.parentElement){
            //console.log(this.el.parentElement.getAttribute("data-column-name"));
        }
   	    if(props.value){
   	     	var row_data=props.grid.getRow(props.rowKey);
			var src_amt = row_data[this.src];
			var tgt_amt = row_data[this.tgt];

            if(!isNaN(src_amt)){
                src_amt = Number(src_amt);
            }

            if(!isNaN(tgt_amt)){
                tgt_amt = Number(tgt_amt);
            }
			
   	    	var tmp=String(props.value);
            var tmp2 = pjtUtil.numberComma(tmp);
            this.el.innerText = tmp2;
     
            if(src_amt<tgt_amt){
        
            	this.el.style="color:#f10808";
            } else if(src_amt>tgt_amt){
            	this.el.style="color:#0080ff";
            } 
		}  else {
			this.el.innerText = props.value;
		}	
    }
  }