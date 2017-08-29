import React from 'react';
import { connect } from 'dva';

class Back extends React.Component {

  render() {
    return null;
  }
}

export default connect(({ main }) => { return { main }; })(Back);
