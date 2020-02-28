import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';

@connect(state => ({ ...state.base }))
class Welcome extends React.Component {
  static propTypes = {
    functionMode: PropTypes.string,
  };

  render() {
    const { functionMode } = this.props;
    return (
      <div>
        {functionMode !== '' && (
          <Redirect
            to={`/${functionMode === 'naming' ? 'serviceManagement' : 'configurationManagement'}`}
          />
        )}
      </div>
    );
  }
}

export default Welcome;
