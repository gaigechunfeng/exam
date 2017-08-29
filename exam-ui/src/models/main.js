import * as C from '../utils/Const';
import { validateForm, submitForm } from '../utils/Fun';

export default {

  namespace: 'main',

  state: {
    user: null,
    pageName: C.PAGE_LOGIN,
  },

  subscriptions: {
    setup({dispatch, history}) {  // eslint-disable-line
    },
  },

  effects: {
    *login({ form }, { call, put }) {
      if (validateForm(form)) {
        const r = yield submitForm(form, '/back/login.do');
        if (r && r.success) {
          yield put({ type: 'setState', payload: { user: r.obj } });
        }
      }
    },
    *fetch({payload}, {call, put}) {  // eslint-disable-line
      yield put({ type: 'save' });
    },
  },

  reducers: {
    save(state, action) {
      return { ...state, ...action.payload };
    },
  },

};
