# LPSolver

Come scaricare le librerie per LPSolver:

Scaricare i seguenti 3 file

- https://sourceforge.net/projects/lpsolve/files/lpsolve/5.5.2.5/lp_solve_5.5.2.5_java.zip/download


  (SELEZIONARE I DOWNLOAD CON IL SISTEMA OPERATIVO UTILIZZATO)
- lp_solve_5.5.2.5_dev_<SO>.zip/download
- lp_solve_5.5.2.5_exe_<SO>.zip/download
  (DISPONIBILI PRESSO IL SEGUENTE LINK https://sourceforge.net/projects/lpsolve/files/lpsolve/5.5.2.5/)
  
Una volta scaricati i file estrarre il contenuto degli zip DEV e EXE dentro la cartella in cui di solito sono aggiunte i file di libreria,
per esempio su Windows (\WINDOWS\SYSTEM32) o su Linux (/usr/local/lib).

Il file Java scaricato contiene due file fondamentali sotto la cartella /lib/:
- lpsolve55j.jar
- /<nome_sistema_operativo>/lpsolve55j.dll (o .so per i SO Unix)

Il file lpsolve55j.jar deve essere aggiunto al BUILDPATH del progetto, ovvero:
(es. da Eclipse)
-> Click tasto destro sul progetto nella sezione Project Explorer
-> Click su "Build Path"
-> Click su "Add External Jar"
-> Selezionare il file lpsolve55j.jar

Il file lpsolve55j.dll (.so su Unix) deve essere aggiunto  dentro la cartella in cui di solito sono aggiunte i file di libreria,
per esempio su Windows (\WINDOWS\SYSTEM32) o su Linux (/usr/local/lib).



  
