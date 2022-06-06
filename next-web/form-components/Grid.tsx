import React, { useState,useEffect,useMemo,forwardRef, useImperativeHandle , CSSProperties } from "react";
import DataGrid, { SelectColumn,SortIconProps,CheckboxFormatterProps, SortColumn, RowsChangeData ,TextEditor, CalculatedColumn, } from 'react-data-grid';
import cloneDeep from 'lodash/cloneDeep';
import {css} from '@emotion/css'
import send, { IParams,ISend } from "@/utils/send";
export declare type Maybe<T> = T | undefined | null;

const toolbarClassname = css`
  text-align: end;
  margin-block-end: 8px;
`;

//https://adazzle.github.io/react-data-grid/#/common-features
//https://github.com/adazzle/react-data-grid
//https://github.com/adazzle/react-data-grid/blob/main/website/demos/CustomizableComponents.tsx
//https://stackoverflow.com/questions/62210286/declare-type-with-react-useimperativehandle
const ROW_STATUS = "_row_status"



interface IGridProps {
    columns:any;
    checkbox?:boolean;
    style?: CSSProperties | undefined;
    onRowsChange?: (rows: any[], data: RowsChangeData<any, any>) => void | undefined | null
    showRowStatus?:boolean;
    flagSummaryRows?:boolean;
    onRowClick?: Maybe<(row: any, column: CalculatedColumn<any, any>) => void>;
    onRowDoubleClick?: Maybe<(row: any, column: CalculatedColumn<any, any>) => void>;
    rows?: any[];
}

interface SummaryRow {
    id: string;
    totalCount: number;
  }
  

function inputStopPropagation(event: React.KeyboardEvent<HTMLInputElement>) {
  if (['ArrowLeft', 'ArrowRight'].includes(event.key)) {
    event.stopPropagation();
  }
}

function rowKeyGetter(row: any) {
    return row._key;  /*원래는 row.id 였는데 키를 구분지을 만한게 없다.   그래서 다 반환하는걸로 원래되로 생각하려면 보이지 않는 column을 만들고  uuid를 넣어야할것 같다. */
}
  
export type GridHandler = {
    getCheckedData: () => any;
    getData: () => any;
    addRow:()=>void;
    loadData: (sendOption:ISend)=>void;
    setData:(rows:any[])=>void;
    getModifiedRows:()=>any;
    
};


const Grid =  forwardRef<GridHandler, IGridProps>((props, ref)=> {
    //console.log('Grid');
    const [columns,setColumns] = useState<any>([])
    const [rows,setRows] = useState<any>([])
    const [selectedRows, setSelectedRows] = useState<ReadonlySet<any>>(() => new Set());
    const [sortColumns, setSortColumns] = useState<readonly SortColumn[]>([]);
   
    useImperativeHandle(ref, () => ({
        getCheckedData() {
            var tmp:any = [];
            console.log({rows})
            selectedRows.forEach((a)=>{
                console.log(a)
                const tmp2 = rows.filter((row:any)=>row._key==a)[0]
                console.log(tmp2)
                if(tmp2!=undefined) {
                    tmp.push(tmp2)
                }

                
            })
            return tmp;
        }
        ,addRow() {
            //console.log("addRow");
            let nextKey = 1;
            if(rows.length>0){
                const max= rows.map((m:any):number=> m._key);
                nextKey = Math.max(...max)+1;
            }
            setRows([
                ...rows,
                
                {"_row_status": "C",_key:nextKey}
                ]
            )
        },
        loadData(sendOption:ISend) {


            const getData = async ()=>{
                const data:any= await send(sendOption.BR, sendOption.PARAM)
                let brRs = sendOption.PARAM.brRs.split(",");
                //console.log(brRs.length)
                const tmp = (data[brRs[0]]).map((row:any,index:number)=>{
                        row._key=(index+1);
                        return row;
                })            
                setRows(tmp)
            }
            getData()
        } ,
        setData(rows:any[]){
            //console.log(rows)
            setRows(rows)
        },
        getModifiedRows(){
            /*
            var crt_cnt	= data.createdRows.length;
		    var updt_cnt= data.updatedRows.length;
            */


            var createdRows = rows.filter((a:any)=>{
                if(a["_row_status"]==='C'){
                    return true;
                }
            })
            var updatedRows = rows.filter((a:any)=>{
                if(a["_row_status"]==='U'){
                    return true;
                }
            })

            return {
                createdRows:createdRows,
                updatedRows:updatedRows
            }

        },
        getData(){
            return rows;
        }
      })
    );

    useEffect(()=>{
        const tmp = cloneDeep(props.columns);        
        if(props.showRowStatus==true) {
            tmp.unshift({ key: "_row_status", name: ' ', width: 30 ,minWidth: 30})
        }

        if(props.checkbox==true) {
            tmp.unshift({...SelectColumn})
        }
        /*_key라는 걸  id로 같도록 하자.  */
        /*hidden:true라는 게 동작하지 않는다. 그래서 width:0, minWidth:0을 해줘서 숨겨준다. 
          굳이 컬럼명을 써서 표시하지 않아도 데이터 row에 _key값을 세팅하면 키로 쓸수도 있고
          데이터를 식별하는 키로 사용해도 문제가 되지 않는다.
        */
        //tmp.push({ key: '_key', name: '',width:0 ,minWidth :0,hidden:true })
        /*예제와 동일한 필터는 성공했는데  필터를 하면  정렬이 안된다. 
        UI를 다르게해서 나중에 하자 지금 필요 없음
        */
       
        //console.log(tmp)
        //console.log(tmp)
        setColumns(tmp)
    },[props.columns])

    useEffect(()=>{
        if(props.rows){
            setRows(props.rows)
        } else {
            setRows([])
        }
    },[props.rows])


    function getComparator(sortColumn: string): Comparator {
          return (a, b) => {
            const tmp = columns.filter((m:any)=>(m.key==sortColumn));
            if(tmp.length>0){
                const isExists ="dataType" in tmp[0]
                if(isExists){
                    const dateType = (tmp[0] as any).dataType
                    if(dateType==="number"){
                        return a[sortColumn] - b[sortColumn];
                    }
                } 
            }
            return a[sortColumn].localeCompare(b[sortColumn]);
          };
    }

    const sortedRows = useMemo((): readonly any [] => {
        //console.log('sortedRows');
        if (sortColumns.length === 0) return rows;
        //console.log(sortColumns)
    
        return [...rows].sort((a, b) => {            
          for (const sort of sortColumns) {
            const comparator = getComparator(sort.columnKey);
            const compResult = comparator(a, b);
            if (compResult !== 0) {
              return sort.direction === 'ASC' ? compResult : -compResult;
            }
          }
          return 0;
        });
      }, [rows, sortColumns]);


      const summaryRows = useMemo(() => {
        //console.log('summaryRows');

        if(props.flagSummaryRows==true){
            const summaryRow: SummaryRow = {
                id: 'total_0',
                totalCount: rows.length
              };
              return [summaryRow];
        }
        
      }, [rows]);

      const gridElement = (
        <DataGrid columns={columns} rows={sortedRows}
              style={props.style} 
              className="fill-grid"
              rowKeyGetter={rowKeyGetter}
              sortColumns={sortColumns}
              onSortColumnsChange={setSortColumns}
              selectedRows={selectedRows}
              onSelectedRowsChange={setSelectedRows}
              onRowsChange={setRows}
              components={{ sortIcon: SortIcon, checkboxFormatter: CheckboxFormatter }}              
              summaryRows={summaryRows}
              onRowClick = {props.onRowClick}
              onRowDoubleClick = {props.onRowDoubleClick}
              defaultColumnOptions={{
                sortable: true,
                resizable: true
              }}
        />
      );

    return (
            <>
                <div className={toolbarClassname}>
                    <button>CSV</button>
                    <button>XSLX</button>
                    <button>PDF</button>
                </div>
                {gridElement}
            </>
          
    )
})


const CheckboxFormatter = forwardRef<HTMLInputElement, CheckboxFormatterProps>(
    function CheckboxFormatter({ disabled, onChange, ...props }, ref) {
      function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        onChange(e.target.checked, (e.nativeEvent as MouseEvent).shiftKey);
      }
  
      return <input type="checkbox" ref={ref} {...props} onChange={handleChange} />;
    }
);
  
function SortIcon({ sortDirection }: SortIconProps) {
    return sortDirection !== undefined ? <>{sortDirection === 'ASC' ? '\u2B9D' : '\u2B9F'} </> : null;
}

type Comparator = (a: any, b: any) => number;

export default Grid;

