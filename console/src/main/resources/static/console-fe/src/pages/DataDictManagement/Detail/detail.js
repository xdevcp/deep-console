import React from 'react';
import PropTypes from 'prop-types';
import { request } from '@/globalLib';
import { Button, Input, ConfigProvider, Form, Loading, Message } from '@alifd/next';
import EditDialog from './EditDialog';
import { getParameter } from 'utils/nacosutil';
import MonacoEditor from 'components/MonacoEditor';
import { MONACO_READONLY_OPTIONS, METADATA_ENTER } from './constant';
import './Detail.scss';

const FormItem = Form.Item;
const pageFormLayout = {
  labelCol: { fixedSpan: 10 },
  wrapperCol: { span: 14 },
};

@ConfigProvider.config
class DataDictDetail extends React.Component {
  static displayName = 'DataDictDetail';

  static propTypes = {
    locale: PropTypes.object,
    history: PropTypes.object,
    location: PropTypes.object,
  };

  constructor(props) {
    super(props);
    this.editDialog = React.createRef();
    this.state = {
      dataType: getParameter(props.location.search, 'type'),
      dataCode: getParameter(props.location.search, 'code'),
      loading: false,
      currentPage: 1,
      instances: {},
      source: {},
      pageSize: 10,
      pageNum: {},
    };
  }

  componentDidMount() {
    if (!this.state.dataType) {
      this.props.history.goBack();
      return;
    }
    this.getDetail();
  }

  getDetail() {
    const { dataType, dataCode } = this.state;
    request({
      url: `v1/open/details?unicode=utf-8&q=${dataType}&w=${dataCode}`,
      beforeSend: () => this.openLoading(),
      success: res => {
        if (res.success === true) {
          const source = res.data || {};
          this.setState({ source });
        } else {
          Message.error(res.errMsg);
        }
      },
      error: e => Message.error(e.responseText || 'error'),
      complete: () => this.closeLoading(),
    });
  }

  openLoading() {
    this.setState({ loading: true });
  }

  closeLoading() {
    this.setState({ loading: false });
  }

  openEditDialog() {
    this.editDialog.current.getInstance().show(this.state.source);
  }

  render() {
    const { locale = {} } = this.props;
    const { loading, source = {} } = this.state;
    const { metadata = {} } = source;
    let metadataText = '';
    if (Object.keys(metadata).length) {
      metadataText = JSON.stringify(metadata, null, '\t');
    }
    return (
      <div className="main-container main-detail">
        <Loading
          shape={'flower'}
          tip={'Loading...'}
          className="loading"
          visible={loading}
          color={'#333'}
        >
          <h1
            style={{
              position: 'relative',
              width: '100%',
            }}
          >
            {locale.detailsTitle}
            <Button
              type="primary"
              className="header-btn"
              onClick={() => this.props.history.goBack()}
            >
              {locale.back}
            </Button>
            <Button type="normal" className="header-btn" onClick={() => this.openEditDialog()}>
              {locale.edit}
            </Button>
          </h1>

          <Form {...pageFormLayout}>
            <FormItem label={`${locale.field1}:`}>
              <Input value={source.dataType} readOnly />
            </FormItem>
            <FormItem label={`${locale.field2}:`}>
              <Input value={source.dataCode} readOnly />
            </FormItem>
            <FormItem label={`${locale.field3}:`}>
              <Input value={source.dataValue} readOnly />
            </FormItem>
            <FormItem label={`${locale.field4}:`}>
              <Input value={source.sortNo} readOnly />
            </FormItem>
            <FormItem label={`${locale.field5}:`}>
              <Input value={source.status} readOnly />
            </FormItem>
            <FormItem label={`${locale.field6}:`}>
              <Input value={source.updateTime} readOnly />
            </FormItem>
            <FormItem label={`${locale.field7}:`}>
              <MonacoEditor
                language="text"
                width={'100%'}
                height={200}
                value={source.dataDesc}
                options={MONACO_READONLY_OPTIONS}
              />
            </FormItem>
          </Form>
        </Loading>
        <EditDialog
          ref={this.editDialog}
          openLoading={() => this.openLoading()}
          closeLoading={() => this.closeLoading()}
          getDetail={() => this.getDetail()}
        />
      </div>
    );
  }
}

export default DataDictDetail;
