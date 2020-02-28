import request from '../utils/request';
import { GET_STATE } from '../constants';

const initialState = {
  version: null,
  standaloneMode: '',
  functionMode: '',
};

const getState = () => dispatch =>
  request
    .get('v1/console/server/state')
    .then(res => {
      dispatch({
        type: GET_STATE,
        data: {
          version: res.version,
          standaloneMode: res.standalone_mode,
          functionMode: res.function_mode,
        },
      });
    })
    .catch(() => {
      dispatch({
        type: GET_STATE,
        data: {
          version: null,
          functionMode: null,
        },
      });
    });

export default (state = initialState, action) => {
  switch (action.type) {
    case GET_STATE:
      return { ...state, ...action.data };
    default:
      return state;
  }
};

export { getState };
