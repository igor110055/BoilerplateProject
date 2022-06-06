class colorCellRenderer {
  constructor(props) {
    const el = document.createElement('div');
    const {targetColumnName, selectedValue ,colorClass, colorColumns,bgColor} = props.columnInfo.renderer.options;
    el.className="tui-grid-cell-content"
    this.el = el;
    this.selectedValue=selectedValue;
    this.colorClass=colorClass;
    this.colorColumns=colorColumns;
    this.bgColor=bgColor;
    this.render(props);
  }
  getElement() {
  return this.el;
  }

  render(props) {
    var row_data=props.grid.getRow(props.rowKey);
    this.el.innerText = String(props.value);
    var rowKey = props.rowKey;
    var colorClass = this.colorClass;
    var colorColumns = this.colorColumns;
    var bgColor = this.bgColor;
    if(props.value==this.selectedValue){
      //그리드로 접근하면 무한 render가 되서 먹통됨
      //props.grid.addRowClassName(rowKey,selectedClass);
      //console.log('aaaa')
      $(this.el).removeClass(colorClass);
      for(var i=0;i<colorColumns.length;i++) {
        var columnName=colorColumns[i];
        var td=props.grid.el.querySelector('[data-row-key="'+rowKey+'"][data-column-name="'+columnName+'"] div')
        if(bgColor){
            $(td).attr('style', 'background-color: '+bgColor);
        } else {
            $(td).attr('style', 'background-color: coral');
        }

        
      }
    } else {
      for(var i=0;i<colorColumns.length;i++) {
        var columnName=colorColumns[i];
        var td=props.grid.el.querySelector('[data-row-key="'+rowKey+'"][data-column-name="'+columnName+'"] div')
        $(td).attr('style', 'background-color: ""');
      }
    }
  }
}