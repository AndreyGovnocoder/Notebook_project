unit main;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, ShellApi, Vcl.Imaging.pngimage,
  Vcl.ExtCtrls, Vcl.StdCtrls, Vcl.ComCtrls;

type
  TMainForm = class(TForm)
    Image1: TImage;
    ProgressBar1: TProgressBar;
    Label1: TLabel;
    Timer1: TTimer;
    Image2: TImage;
    Label2: TLabel;
    Label3: TLabel;
    Label4: TLabel;
    Label5: TLabel;
    Label6: TLabel;
    Label7: TLabel;
    Label8: TLabel;
    Label9: TLabel;
    procedure FormShow(Sender: TObject);
    procedure FormKeyPress(Sender: TObject; var Key: Char);
    procedure Timer1Timer(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  MainForm: TMainForm;
  start: boolean;

implementation
const
  java = 'JRE\bin\javaw.exe';
  params = ' -jar launchApp.jar';

{$R *.dfm}

procedure TMainForm.FormKeyPress(Sender: TObject; var Key: Char);
begin
  if (Key = #13) and (start = true) then
  begin
    ShellExecute(handle, 'open', java, params, nil, SW_RESTORE);
    MainForm.Close;
  end;
end;

procedure TMainForm.FormShow(Sender: TObject);
begin
   start:= false;
end;

procedure TMainForm.Timer1Timer(Sender: TObject);
begin
  if ProgressBar1.Position < 5000 then
    ProgressBar1.Position := ProgressBar1.Position + 250;

  if ProgressBar1.Position = 5000 then
  begin
    start:= true;
    Label1.Visible := true;
    if Label1.Font.Color = clWindowText then
        Label1.Font.Color := clBlue
    else
        Label1.Font.Color := clWindowText;

  end;

end;

end.