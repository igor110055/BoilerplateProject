import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';
import { FX_0160__업비트주문_sub1 } from './FX_0160__업비트주문_sub1';
import { FX_0160__업비트주문_sub2 } from './FX_0160__업비트주문_sub2';




export const FX_0160__업비트주문= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    return (
            <>
            <table>
            <tr>
                <td><FX_0160__업비트주문_sub1  /></td>
                <td width="30"></td>
                <td><FX_0160__업비트주문_sub2  /></td>
            </tr>
            </table>            
            </>
    )
  }


