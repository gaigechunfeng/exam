import React from 'react';
import { connect } from 'dva';
import { resetForm, validateForm } from '../utils/Fun';
import style from './css/login.css';
import '../index.css';

class Login extends React.Component {

  render() {

    const {dispatch} = this.props
    return (
      <table width="100%" className="border0" cellSpacing="0" cellPadding="0">

        <tbody>
          <tr>
            <td height="117" className="backgroundRepeat alignCenter valignBottom">
              <table width="660" height="42" className="border0 marginCenter" cellPadding="0" cellSpacing="0">
                <tbody>
                  <tr>
                    <td width="20">&nbsp;</td>
                    <td width="69"><img src="imges/fahui.gif" alt="图标" width="69" height="69" /></td>
                    <td width="571" className="alignLeft"><span className={style.STYLE1}>书记员录入能力机考系统<span className={style.STYLE7}>|<span
                      className={style.STYLE9}
                    >后台管理</span></span></span></td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
          <tr>
            <td height="29" className="backgroundRepeat alignCenter">&nbsp;</td>
          </tr>
          <tr>
            <td
              height="438" className="backgroundRepeat alignCenter valignTop"
              style={{ background: 'url(imges/bg.jpg)' }}
            >
              <form id="form1" name="form1" method="post" action="">
                <table width="520" className="border0 marginCenter" cellPadding="0" cellSpacing="0">
                  <tbody>
                    <tr>
                      <td height="431" className={style.border1}>
                        <table width="520" className="border0" cellSpacing="0" cellPadding="0">
                          <tbody>
                            <tr>
                              <td
                                width="520" height="193"
                                className="valignBottom alignRight"
                                style={{ background: 'url(imges/login.jpg)' }}
                              >
                                <table width="159" height="38" className="border0" cellPadding="0" cellSpacing="0">
                                  <tbody>
                                    <tr>
                                      <td className="alignCenter valignMiddle" ><span className={style.STYLE6}>管理员登录平台&gt;&gt;</span></td>
                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td height="243" className={`${style.bgColor3} valignTop`} >
                                <table id="__01" width="520" height="204" className="border0" cellPadding="0" cellSpacing="0">
                                  <tbody>
                                    <tr>
                                      <td colSpan="5" height="23">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td rowSpan="7" width="67" height="177">&nbsp;</td>
                                      <td width="282" height="33" className={style.bgColor4}>
                                        <table width="282" height="27" className="border0" cellPadding="0" cellSpacing="0">
                                          <tbody>
                                            <tr>
                                              <td width="93" className="alignCenter valignMiddle"><span className={style.STYLE2}>管理账户</span>
                                              </td>
                                              <td width="187" className="whiteBgColor"><input
                                                label="用户名"
                                                type="text" name="username"
                                                className={style.input1}
                                              />
                                              </td>
                                              <td width="2" />
                                            </tr>
                                          </tbody>
                                        </table>
                                      </td>
                                      <td rowSpan="3" width="9" height="80" />
                                      <td width="91" rowSpan="3" className="alignCenter"><img
                                        src="imges/gly.gif" alt="管理员图标" width="87"
                                        height="76"
                                        className={style.border2}
                                      />
                                      </td>
                                      <td rowSpan="7" width="71" height="177">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td height="11" />
                                    </tr>
                                    <tr>
                                      <td height="33" className={style.bgColor4}>
                                        <table width="282" height="27" className="border0" cellPadding="0" cellSpacing="0">
                                          <tbody>
                                            <tr>
                                              <td width="93" className="alignCenter valignMiddle"><span className={style.STYLE2}>管理密码</span>
                                              </td>
                                              <td width="187" className="whiteBgColor"><input
                                                label="密码"
                                                type="password"
                                                name="password"
                                                className={style.input1}
                                              />
                                              </td>
                                              <td width="2" />
                                            </tr>
                                          </tbody>
                                        </table>
                                      </td>
                                    </tr>
                                    <tr>
                                      <td colSpan="3" height="18">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td
                                        height="40" colSpan="3"
                                        className={`${style.bgColor1} alignCenter pointerCursor `}
                                        onClick={(e) => {
                                          const form = $(e.target).parents('form');

                                          dispatch({ type: 'main/login', form });
                                          if (validateForm(form)) {

                                          }
                                        }}
                                      ><span className={style.STYLE3}>登&nbsp;&nbsp;
                                陆</span></td>
                                    </tr>
                                    <tr>
                                      <td colSpan="3" height="8" />
                                    </tr>
                                    <tr>
                                      <td
                                        height="40" colSpan="3"
                                        className={`${style.bgColor2} ${style.STYLE3} alignCenter pointerCursor`}
                                        onClick={(e) => {
                                          resetForm($(e.target).parents('form'));
                                        }}
                                      >重&nbsp;&nbsp;置
                              </td>
                                    </tr>
                                    <tr>
                                      <td colSpan="5" height="28">&nbsp;</td>
                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </form>
            </td>
          </tr>
          <tr>
            <td height="60" className="bottom alignCenter valignMiddle"><span
              className="STYLE4"
            >白银市中级人民法院</span>.信息办 版权所有 Tel:0943-8268673
          </td>
          </tr>
        </tbody>
      </table>
    );
  }
}

export default connect(({ main }) => {
  return { main };
})(Login);
