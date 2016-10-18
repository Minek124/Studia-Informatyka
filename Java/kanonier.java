class Kanonier extends KanonierA {

    private DzialoI myCannon; //ref do dzialka
    private SystemProwadzeniaOgniaI mySpo; // ref do spo
    private double maxV;
    private double minV;
    private double minAngle;
    private double maxAngle;

    private int shot(double v1,double angle1) {
        myCannon.setPredkoscWylotowaPocisku(v1);
        myCannon.setKatPodniesieniaLufy(angle1);
        myCannon.ognia();
        return mySpo.getWynikStrzelania();
    }

    @Override
    public void uzbrojenie(SystemProwadzeniaOgniaI spo) {
        myCannon = spo.getDzialo();
        mySpo = spo;
        maxV = myCannon.getMaksymalnaPredkoscWylotowaPocisku();
        minV = myCannon.getMinimalnaPredkoscWylotowaPocisku();
        maxAngle = myCannon.getMaksymalnyKatPodniesieniaLufy();
        minAngle = myCannon.getMinimalnyKatPodniesieniaLufy();
    }

    @Override
    public void zniszczCel() {
        int info;
        double angle=(minAngle + maxAngle) / 2;
        double v=maxV;
        double addV;
        for(double i=minAngle;i<=maxAngle;i=i+(maxAngle/10)){ //ustawienie precyzji max 10 dodatkowych shotow
            angle=i; 
            info=shot(v,angle);
            if(info!=-1){// pewnosc ze zmieniajac v znajdziemy cel
                break;
            }           
        }
        v = (minV + maxV) / 2;
        addV=(maxV-minV)/4;
        info = shot(v,angle);
        while (true) {
            if(info==0){
                break;
            }
            if(info==-1){
                v=v+addV;
            }
            if(info==1){
                v=v-addV;
            }
            addV/=2;
            info=shot(v,angle);
        }
    }
}