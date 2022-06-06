import Grid,{GridHandler} from '@/form-components/Grid';
import React, { useEffect,useRef,useReducer,useContext, useState } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import { CellExpanderFormatter } from '@/form-components/formatters/CellExpanderFormatter';
import { CalculatedColumn } from 'react-data-grid';
import {MenuContext} from "@/store/MenuStore";
import { FormSelect } from '@/form-components/FormSelect';
import { useForm } from "react-hook-form";
import GridCombo from '@/form-components/GridCombo';

interface IFormInput {
    ROLE_CD:string;
}

const defaultValues = {
    ROLE_CD:""
};

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

export  const CM_4200__역할관리_tree = () => {   
    const [roleCdData,setRoleCdData] = useState([]);
    const menuStoreContext = useContext(MenuContext)
    const [useYnData,setUseYnData] = useState([]);
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

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
    const fnSearch= async (param:IFormInput) => {
        if(childRef.current){
            const data:any= await send("BR_CM_ROLE_CD_MENU_FIND_TREE", {
                 brRq: 'IN_DATA' 
                ,brRs: 'OUT_DATA'
                ,IN_DATA:[param]
            });
            console.log({data})
            if(data.OUT_DATA.length == 0){
                return;

            }
            var tmp_data =make_menu_tree_re_order(data.OUT_DATA)
            console.log(tmp_data)
            dispatch({ payload: tmp_data, type: 'search' })
        }
    }
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_ROLE_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  {}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["ROLE_CD"],
                    text: m["ROLE_NM"]            
                };
            })
            setRoleCdData(tmp)
        }
        getData();

        const getData2 = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'USE_YN',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setUseYnData(tmp)
        }
        getData2();
        handleSubmit(fnSearch)
    },[]);

    const saveHandler= async(param:IFormInput) => {
        if(childRef.current){
            const p_data = childRef.current.getData();
            /*너무 많은데이터가 넘어가서 문제가 됨 _row_status는 읽는 도중 에러가 남
             MENU_NO하고 ROLE_CD 하고  ROLE_YN이 Y인 것만 넘아가면 됨
            */
            if(p_data.length<=0) {
                messageAlert('조회된 항목이 없습니다.');
                return;
            }
            /*N인 것도 걸려야 하니까.  유효성은 전체로 */
            const p_data2 = p_data.map((m:any)=>{
                return {MENU_NO:m.MENU_NO,ROLE_CD:m.ROLE_CD,ROLE_YN:m.ROLE_YN}
            }).filter((m:any)=>{
                if(m.ROLE_YN=="Y"){
                    return true
                } else {
                    return false
                }
            })
            messageConfirm("메뉴 권한을 저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_ROLE_CD_MENU_SAVE',{
                    brRq 		: 'IN_ROLE_CD,IN_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_ROLE_CD : [{ROLE_CD: param.ROLE_CD}]
                    ,IN_DATA	: p_data2
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("저장되었습니다",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }
    }

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
        { key: 'ROLE_CD', name: '역할코드', width: 140,  sortable: false},
        { key: 'MENU_KIND', name: '메뉴종류', width: 80,  sortable: false},
        { key: 'ROLE_YN', name: '역할부여', width: 80,  sortable: false   ,editor: (p:any) => (<GridCombo p={p} options={useYnData}  />) , editorOptions: {editOnClick: true}},
    ];

    return (
            <>
                <FormSelect name="ROLE_CD" control={control} label="역할코드" sx={{width:200}} rules={{ required: false }}  options={roleCdData}  />
                <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                <Button variant="contained" color="success"  onClick={handleSubmit(saveHandler)}>저장</Button>      
                <Grid style={{ height: 500, width:  '100%' }} columns={columns}  checkbox={false} showRowStatus={true} ref={childRef} rows={rows} />            
            </>
    )
  }