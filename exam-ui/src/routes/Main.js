import React from 'react';
import { connect } from 'dva';
import Login from '../components/Login';
import Back from '../components/Back';

class Main extends React.Component {

  render() {
    const { main: { user, pageName } } = this.props;
    const isLogin = user !== null;

    if (isLogin) {
      return <Back />;
    } else {
      return <Login />;
    }
  }
}

export default connect(({ main }) => {
  return { main };
})(Main);
