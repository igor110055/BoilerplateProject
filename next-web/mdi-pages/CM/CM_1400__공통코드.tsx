import { CM_1400__공통코드_sub1 } from '@/mdi-pages/CM/CM_1400__공통코드_sub1';
import { CM_1400__공통코드_sub2, CM_1400__공통코드_sub2Handler } from '@/mdi-pages/CM/CM_1400__공통코드_sub2';
import { useRef } from 'react';
import { CalculatedColumn } from 'react-data-grid';


export  const CM_1400__공통코드 = () => {   
    const childRef = useRef<CM_1400__공통코드_sub2Handler>(null);
    const fnSearch= async (grp_cd: string) => {
        if(childRef.current){
            childRef.current.fnSearchGrid(grp_cd);

        }
    }
    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        //console.log(row.GRP_CD);       
        fnSearch(row.GRP_CD);
    }

    return (
            <>
            <CM_1400__공통코드_sub1 onRowClick={gridOnRowClickHandler} />
            <CM_1400__공통코드_sub2 ref={childRef} />
            </>
    )
  }
  
  

