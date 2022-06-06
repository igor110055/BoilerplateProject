import Grid,{GridHandler} from '@/form-components/Grid';
import React, { useEffect,useRef,useReducer,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import { CellExpanderFormatter } from '@/form-components/formatters/CellExpanderFormatter';
import { CM_1300__메뉴관리_sub1 } from '@/mdi-pages/CM/CM_1300__메뉴관리_sub1';
import { CM_1150__프로그램조회팝업 } from '@/mdi-pages/CM/CM_1150__프로그램조회팝업';
import { CalculatedColumn } from 'react-data-grid';
import {MenuContext} from "@/store/MenuStore";

const make_menu_tree_re_order = (data:any[]) =>{
    var al_tmp =[];
    for(var i=0;i<data.length;i++){
        var tmp = data[i];
        if(tmp.MENU_LVL=="0"){
            al_tmp.push(tmp);
            tmp.isExpanded = false;
            make_sub_menu_tree_re_order(data,tmp,tmp.MENU_CD);
        }
    }
    return al_tmp;
}

const make_sub_menu_tree_re_order = (al:any[], c:any, menu_cd:string) =>{
    var al_sub=[];
    for(var i=0;i<al.length;i++){
        var c1 = al[i];
        if(c1.PRNT_MENU_CD==menu_cd){
            al_sub.push(c1);
            c1.isExpanded = false;
            make_sub_menu_tree_re_order(al,c1,c1.MENU_CD);
        }
    }
    if(al_sub.length>0){
        c.children=al_sub;
    }
}
interface Action {
    type: 'toggleSubRow' | 'search';
    id?: string;
    payload?:any[];
  }

  function toggleSubRow(rows:any[], id: string): any[] {
    const rowIndex = rows.findIndex((r:any) => r.MENU_CD === id);
    const row:any = rows[rowIndex];
    const { children } = row;
    if (!children) return rows;
  
    const newRows:any = [...rows];
    newRows[rowIndex] = { ...row, isExpanded: !row.isExpanded };
    if (!row.isExpanded) {
      newRows.splice(rowIndex + 1, 0, ...children);
    } else {
      newRows.splice(rowIndex + 1, children.length);
    }
    return newRows;
  }

export  const CM_1300__메뉴관리 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;

    const  reducer = (rows: any[], { type, id,payload }: Action): any[]  =>{
        switch (type) {
          case 'search':
              return payload!;
          case 'toggleSubRow':
            return toggleSubRow(rows, id!);
          default:
            return rows;
        }
    }

    const childRef = useRef<GridHandler>(null);
    const [rows, dispatch] = useReducer(reducer, []);
    const fnSearch= async () => {
        if(childRef.current){
            const data:any= await send("BR_CM_MENU_FIND_TREE", {brRq: '' ,brRs: 'OUT_DATA'});
            var tmp_data =make_menu_tree_re_order(data.OUT_DATA)
            console.log(tmp_data)
            dispatch({ payload: tmp_data, type: 'search' })
        }
    }
    useEffect(()=>{
        fnSearch()
    },[]);

    const columns = [
        { key: 'MENU_NM', name: '메뉴TREE', width: 220 ,  sortable: false ,   
            formatter(  {row, isCellSelected}:{row:any, isCellSelected:boolean}  ) {
                const hasChildren = row.children !== undefined;
                //const style = !hasChildren ? { marginInlineStart: 30 } : undefined;
                const style = { marginInlineStart:  row.MENU_LVL*10 };
                return (
                <>
                    {hasChildren && (
                    <CellExpanderFormatter
                        isCellSelected={isCellSelected}
                        expanded={row.isExpanded === true}
                        onCellExpand={() => dispatch({ id: row.MENU_CD, type: 'toggleSubRow' })}
                    />
                    )}
                    <div className="rdg-cell-value">
                    <div style={style}>{row.MENU_NM}</div>
                    </div>
                </>
                );
            }
        },
        { key: 'MENU_CD', name: '메뉴코드', width: 150,  sortable: false},
        { key: 'PGM_ID', name: '프로그램ID', width: 120 ,  sortable: false},
        { key: 'ORD', name: '정렬', width: 80,  sortable: false},
    ];

    return (
            <>
            <table>
                <tr>
                    <td style={{verticalAlign:"top",paddingTop:20}}>
                        <Button variant="contained" color="success"   onClick={fnSearch}>조회</Button>
                        <Grid style={{ height: 500, width: 600 }} columns={columns}  checkbox={false} showRowStatus={false} ref={childRef} rows={rows} />            
                    </td>
                    <td style={{verticalAlign:"top"}}>
                        <CM_1300__메뉴관리_sub1 />
                    </td>
                </tr>
            </table>
            </>
    )
  }
  
