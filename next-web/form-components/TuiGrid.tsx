import dynamic from 'next/dynamic';
import { forwardRef, useImperativeHandle, useState } from 'react';
import send, { IParams,ISend } from "@/utils/send";


import "tui-grid/dist/tui-grid.css";
/*import * as TGrid from "@toast-ui/react-grid";*/
const Grid = dynamic(() => import("@toast-ui/react-grid"), { ssr: false });
import { DataSource } from "tui-grid/types/dataSource";
import { OptColumn, OptHeader, OptRow } from "tui-grid/types/options";

interface IGridProps {
    columns:OptColumn[];
    header?: OptHeader;

}

export type TuiGridHandler = {
    getCheckedData: () => any;
    getData: () => any;
    addRow:()=>void;
    loadData: (sendOption:ISend)=>void;
    setData:(rows:any[])=>void;
    getModifiedRows:()=>any;
    
};

const TuiGrid =  forwardRef<TuiGridHandler, IGridProps>((props, ref)=> {
    const [rows,setRows] = useState<any>([])
    const [selectedRows, setSelectedRows] = useState<ReadonlySet<any>>(() => new Set());

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
            console.log('loadData')


            const getData = async ()=>{
                const data:any= await send(sendOption.BR, sendOption.PARAM)
                let brRs = sendOption.PARAM.brRs.split(",");
                console.log({data})
                //console.log(brRs.length)
                const tmp = (data[brRs[0]]).map((row:any,index:number)=>{
                        row._key=(index+1);
                        return row;
                })            
                console.log(tmp)
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


    return (
            <>
                <Grid
                columnOptions={{
                resizable: true
                }}
                data={rows}
                header={props.header}
                treeColumnOptions={{ name: "server_host" }}
                columns={props.columns}
                minRowHeight={50}
                /*bodyHeight={400}*/
                showDummyRows={false}
                rowHeight={"auto"}
                pageOptions={{
                useClient: true,
                perPage: 14
                }}
                scrollX={true}
                scrollY={false}
                heightResizable={false}
            />
            </>
          
    )
})

export default TuiGrid;

