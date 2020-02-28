
import * as React from 'react';
import { MONACO_OPTIONS } from './constant';
import './index.scss';

interface PropsType {
  dispatch: (obj: Object) => void;
  options: Object;
  value: string;
  language: string;
  width: number;
  height: number;
  onChange: (value: string) => void;
}
interface StateType {}

class MonacoEditor extends React.Component<PropsType, StateType> {
  static displayName = 'MonacoEditor';

  private nodeRef: any = React.createRef();
  public monacoEditor: any = null;
  public state: StateType;
  public props: PropsType;

  constructor(props: PropsType) {
    super(props);
  }

  componentWillReceiveProps(nextProps): void {
    if (!this.monacoEditor) {
      return;
    }

    const { value = '', language = 'js', width, height, options = {} } = this.props;

    if (value !== nextProps.value) {
      this.monacoEditor.setValue(nextProps.value || '');
    }
    if (language !== nextProps.language) {
      this.monacoEditor.editor.setModelLanguage(this.monacoEditor.getModel(), nextProps.language);
    }

    if (this.monacoEditor && (width !== nextProps.width || height !== nextProps.height)) {
      this.monacoEditor.layout();
    }
    if (this.monacoEditor && nextProps.options && options !== nextProps.options) {
      this.monacoEditor.updateOptions({ ...MONACO_OPTIONS, ...nextProps.options });
    }
  }

  componentDidMount(): void {
    if (!window.monaco) {
      window.importEditor &&
        window.importEditor(() => {
          this.initMoacoEditor();
        });
    } else {
      this.initMoacoEditor();
    }
  }

  componentWillUnmount(): void {
    this.monacoEditor && this.monacoEditor.dispose();
    this.nodeRef = null;
  }

  initMoacoEditor(): void {
    const { options = {}, language = 'js', value = '' } = this.props;
    try {
      this.monacoEditor = window.monaco.editor.create(this.nodeRef && this.nodeRef.current, {
        ...MONACO_OPTIONS,
        ...options,
        language,
        value,
      });
      this.editorDidMount(this.monacoEditor);
    } catch (error) {}
  }

  editorDidMount(editor: any) {
    const { onChange } = this.props;
    editor.onDidChangeModelContent(event => {
      const value = editor.getValue();

      typeof onChange === 'function' && onChange(value);
    });
  }

  render(): HTMLElement {
    const { width = '100%', height = 0 } = this.props;
    const style = {
      width,
      height,
    };
    return <div ref={this.nodeRef} style={style} />;
  }
}

export default MonacoEditor;
