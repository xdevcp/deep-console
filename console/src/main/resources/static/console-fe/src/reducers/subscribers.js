import request from '../utils/request';
import { GET_SUBSCRIBERS, REMOVE_SUBSCRIBERS } from '../constants';

const initialState = {
  subscribers: {},
};

const getSubscribers = params => dispatch =>
  request.get('v1/ns/service/subscribers', { params }).then(data => {
    dispatch({
      type: GET_SUBSCRIBERS,
      data,
    });
  });
const removeSubscribers = () => dispatch => dispatch({ type: REMOVE_SUBSCRIBERS });

export default (state = initialState, action) => {
  switch (action.type) {
    case GET_SUBSCRIBERS:
      return { ...state, ...action.data };
    case REMOVE_SUBSCRIBERS:
      return { ...state, subscribers: {} };
    default:
      return state;
  }
};

export { getSubscribers, removeSubscribers };
