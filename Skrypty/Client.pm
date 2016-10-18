
package Client;
use IO::Socket::INET;

sub new
{
	my ($class, $args) = @_; 
    my $self = {
        s => $args->{s},
        name => $args->{name},
        power => 0,
        type => 0,
		sel => $args->{sel},
    };
    return bless $self, $class;
}
sub recv {
    my $self = shift;
    my $data = ""; 
	$self->{s}->recv($data, 1024);
	return $data;
}
sub send {
	my ($self, $m) = @_;
	$self->{s}->send($m);
}
sub close {
   my $self = shift;
   $self->{s}->close();
}
1;